package cz.muni.fgdovin.bachelorthesis.web;

import cz.muni.fgdovin.bachelorthesis.esper.EsperUserFriendlyService;
import cz.muni.fgdovin.bachelorthesis.rabbit.RabbitMqService;
import cz.muni.fgdovin.bachelorthesis.support.DataflowHelper;
import cz.muni.fgdovin.bachelorthesis.support.EventTypeHelper;

import org.nigajuan.rabbit.management.client.domain.exchange.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides web application interface, it reflects
 * methods of EsperService to provide convenient and user-friendly
 * way to configure Esper. Forms are used to collect data from
 * user and to set Esper accordingly.
 *
 * @author Filip Gdovin
 * @version 4. 3. 2015
 */

@SuppressWarnings("SpringMVCViewInspection") //provided by Spring Boot, Idea doesn't like it and cries about being unable to find view resolvers
@Controller
@RequestMapping(value = "/")
public class WebApi {

    @Autowired
    private EsperUserFriendlyService esperUserFriendlyService;

    @Autowired
    private DataflowHelper dataflowHelper;

    @Autowired
    private EventTypeHelper eventTypeHelper;

    @Autowired
    private RabbitMqService rabbitMqService;

    /**
     * This method is bound to "/" mapping and displays welcome page.
     *
     * @return Welcome page with available commands.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {
        boolean serverOK = this.rabbitMqService.isAlive().equals("ok");

        ModelAndView model = new ModelAndView("index");
        model.addObject("rabbitMqInfo", serverOK);
        return model;
    }

    /**
     * This method is bound to "/manageInputDataflows" mapping and displays possible input dataflows options.
     *
     * @return Page with available commands containing list of currently defined input dataflows.
     */
    @RequestMapping(value = "/manageInputDataflows", method = RequestMethod.GET)
    public ModelAndView manageInputDataflows() {
        List<String> list = esperUserFriendlyService.showInputDataflows();

        ModelAndView model = new ModelAndView("manageInputDataflows");
        model.addObject("allInputDataflows", list);
        return model;
    }

    /**
     * This method is bound to "/manageOutputDataflows" mapping and displays possible output dataflows options.
     *
     * @return Page with available commands containing list of currently defined output dataflows.
     */
    @RequestMapping(value = "/manageOutputDataflows", method = RequestMethod.GET)
    public ModelAndView manageOutputDataflows() {
        List<String> list = esperUserFriendlyService.showOutputDataflows();

        ModelAndView model = new ModelAndView("manageOutputDataflows");
        model.addObject("allOutputDataflows", list);
        return model;
    }

    /**
     * This method is called when user decides to add new input dataflow based on event type.
     *
     * @return Web page containing form for input dataflow creation.
     */
    @RequestMapping(value = "/addInputDataflow", method = RequestMethod.GET)
    public ModelAndView InputDataflowForm() {
        ModelAndView model = new ModelAndView("addInputDataflow", "EventTypeModel", new EventTypeModel());
        List<Exchange> esperExchanges = this.rabbitMqService.listExchanges();
        Map<String, Object> exchangesWithSchemas = new HashMap<>();
        for(int i = 0; i < esperExchanges.size(); i++) {
            Exchange oneExchange = esperExchanges.get(i);
            Map<String, Object> schema = this.rabbitMqService.getSchemaForExchange(oneExchange.getName());
            exchangesWithSchemas.put(esperExchanges.get(i).getName(), schema);
        }
        System.out.println(exchangesWithSchemas);
        model.addObject("availExchanges", exchangesWithSchemas);
        return model;
    }

    /**
     * This method is called when user submits a form, it POSTs form contents
     * so that new event type and its input dataflow can be created.
     *
     * @param modelClass Model class representing fields in form, form contents are mapped to instance of this class.
     * @param resultModel ModelMap containing input dataflow information, it will be displayed as confirmation of creation.
     * @return Page displaying newly created input dataflow.
     */
    @RequestMapping(value = "/addInputDataflow", method = RequestMethod.POST)
    public String submitInputDataflowForm(@ModelAttribute("EventTypeModel") EventTypeModel modelClass, ModelMap resultModel) {
        String dataflowName = modelClass.getEventType();

        System.out.println("Map of prop is " + modelClass.getMapProperties());

        boolean added = false;//esperUserFriendlyService.addEventType(dataflowName, properties);
        resultModel.addAttribute("dataflowName", dataflowName);

        if(!added) {
            resultModel.addAttribute("result", "Error creating input dataflow. Bad input or is already defined!");
            return "addInputDataflowResult";
        }

        added = manageAMQPQueue(dataflowName);

        if(!added) {
            resultModel.addAttribute("result", "Error declaring dataflow. Creation of AMQP queue failed!");
            this.esperUserFriendlyService.removeEventType(dataflowName);
            return "addInputDataflowResult";
        }
        //create dataflow fot this event type

        InputDataflowModel myModel = new InputDataflowModel();
        myModel.setEventType(dataflowName);
        myModel.setDataflowName(dataflowName);
        myModel.setQueueName(dataflowName);

        String queueParams = dataflowHelper.generateInputDataflow(myModel);
        added = esperUserFriendlyService.addDataflow(dataflowName, queueParams);

        if(added) {
            resultModel.addAttribute("result", "Input dataflow created successfully.");
        } else {
            resultModel.addAttribute("result", "Error creating input dataflow. Possible reasons: Bad user input or " +
                    "dataflow with this name already defined!");
        }
        return "addInputDataflowResult";
    }

    /**
     * This method is called when user chooses to delete input dataflow along with its event type.
     *
     * @param dataflowName Name of dataflow to be deleted.
     *
     * @param resultModel ModelMap used to provide information to the View
     *
     * @return Web page containing form to delete input dataflow by its name.
     */
    @RequestMapping(value = "/removeInputDataflow", method = RequestMethod.GET)
    public String removeInputDataflowForm(@RequestParam("dataflowName")String dataflowName, ModelMap resultModel) {
        resultModel.addAttribute("dataflowName", dataflowName);
        return "removeInputDataflow";
    }

    /**
     * This method is called when user submits input dataflow deletion form, it finds input dataflow
     * and deletes it if possible.
     *
     * @param dataflowName Name of dataflow to be deleted.
     *
     * @param resultModel ModelMap containing result of input dataflow deletion.
     * @return Web page informing user if the input dataflow was successfully deleted.
     */
    @RequestMapping(value = "/removeInputDataflow", method = RequestMethod.POST)
    public String submitRemoveInputDataflowForm(@RequestParam("dataflowName")String dataflowName, ModelMap resultModel) {
        resultModel.addAttribute("dataflowName", dataflowName);
        boolean removed = esperUserFriendlyService.removeEventType(dataflowName);

        if (!removed) {
            resultModel.addAttribute("result", "Input dataflow with this name was not found, or is still in use and was not removed.");
            return "removeInputDataflowResult";
        }

        removed = esperUserFriendlyService.removeInputDataflow(dataflowName);

        if (removed) {
            resultModel.addAttribute("result", "No statements rely on this event type, its dataflow removed successfully.");
        } else {
            resultModel.addAttribute("result", "Input dataflow removal failed, its event type removed to avoid undefined behaviour.");
        }
        return "removeInputDataflowResult";
    }

    /**
     * This method is called when user decides to add new one-stream output dataflow.
     *
     * @return Web page containing form for one-stream output dataflow creation.
     */
    @RequestMapping(value = "/addOneStreamOutputDataflow", method = RequestMethod.GET)
    public ModelAndView oneStreamOutputDataflowForm() {
        ModelAndView model = new ModelAndView("addOneStreamOutputDataflow", "OutputDataflowModel", new OutputDataflowModel());
        model.addObject("availEventTypes", this.esperUserFriendlyService.showEventTypeNames());
        return model;
    }

    /**
     * This method is called when user submits a form, it POSTs form contents
     * so that new one-stream output dataflow can be created.
     *
     * @param modelClass Model class representing fields in form, form contents are mapped to instance of this class.
     * @param resultModel ModelMap containing one-stream output dataflow information, it will be displayed as confirmation of creation.
     * @return Page displaying newly created one-stream output dataflow.
     */
    @RequestMapping(value = "/addOneStreamOutputDataflow", method = RequestMethod.POST)
    public String submitOneStreamOutputDataflowForm(@ModelAttribute("OutputDataflowModel") OutputDataflowModel modelClass, ModelMap resultModel) {
        String outputEventType = modelClass.getOutputEventType();
        String dataflowName = outputEventType + System.currentTimeMillis();
        modelClass.setDataflowName(dataflowName);
        modelClass.setQueueName(outputEventType);

        String queueParams = dataflowHelper.generateOutputDataflow(modelClass);
        boolean added = esperUserFriendlyService.addDataflow(dataflowName, queueParams);

        resultModel.addAttribute("dataflowName", dataflowName);
        if(!added) {
            resultModel.addAttribute("result", "Error creating output dataflow. Possible reasons: Bad user input or " +
                    "dataflow with this name already defined!");
            return "addOneStreamOutputDataflowResult";
        }

        added = manageAMQPQueue(outputEventType);

        if(!added) {
            resultModel.addAttribute("result", "Error declaring dataflow. Creation of AMQP queue failed!");
        } else {
            resultModel.addAttribute("result", "Output dataflow created successfully.");
        }
        return "addOneStreamOutputDataflowResult";
    }

    /**
     * This method is called when user decides to add new two-stream output dataflow.
     *
     * @return Web page containing form for two-stream output dataflow creation.
     */
    @RequestMapping(value = "/addTwoStreamOutputDataflow", method = RequestMethod.GET)
    public ModelAndView twoStreamOutputDataflowForm() {
        ModelAndView model = new ModelAndView("addTwoStreamOutputDataflow", "OutputDataflowModel", new OutputDataflowModel());
        model.addObject("availEventTypes", this.esperUserFriendlyService.showEventTypeNames());
        return model;
    }

    /**
     * This method is called when user submits a form, it POSTs form contents
     * so that new two-stream output dataflow can be created.
     *
     * @param modelClass Model class representing fields in form, form contents are mapped to instance of this class.
     * @param resultModel ModelMap containing two-stream output dataflow information, it will be displayed as confirmation of creation.
     * @return Page displaying newly created two-stream output dataflow.
     */
    @RequestMapping(value = "/addTwoStreamOutputDataflow", method = RequestMethod.POST)
    public String submitTwoStreamOutputDataflowForm(@ModelAttribute("OutputDataflowModel") OutputDataflowModel modelClass, ModelMap resultModel) {
        String outputEventType = modelClass.getOutputEventType();
        String dataflowName = outputEventType + System.currentTimeMillis();
        modelClass.setDataflowName(dataflowName);
        modelClass.setQueueName(outputEventType);

        String queueParams = dataflowHelper.generateOutputDataflow(modelClass);
        boolean added = esperUserFriendlyService.addDataflow(dataflowName, queueParams);

        resultModel.addAttribute("dataflowName", dataflowName);
        if(!added) {
            resultModel.addAttribute("result", "Error creating output dataflow. Possible reasons: Bad user input or " +
                    "dataflow with this name already defined!");
            return "addTwoStreamOutputDataflowResult";
        }

        added = manageAMQPQueue(outputEventType);

        if(!added) {
            resultModel.addAttribute("result", "Error declaring dataflow. Creation of AMQP queue failed!");
        } else {
            resultModel.addAttribute("result", "Output dataflow created successfully.");
        }
        return "addTwoStreamOutputDataflowResult";
    }

    /**
     * This method is called when user decides to add new three-stream output dataflow.
     *
     * @return Web page containing form for three-stream output dataflow creation.
     */
    @RequestMapping(value = "/addThreeStreamOutputDataflow", method = RequestMethod.GET)
    public ModelAndView threeStreamOutputDataflowForm() {
        ModelAndView model = new ModelAndView("addThreeStreamOutputDataflow", "OutputDataflowModel", new OutputDataflowModel());
        model.addObject("availEventTypes", this.esperUserFriendlyService.showEventTypeNames());
        return model;
    }

    /**
     * This method is called when user submits a form, it POSTs form contents
     * so that new two-stream output dataflow can be created.
     *
     * @param modelClass Model class representing fields in form, form contents are mapped to instance of this class.
     * @param resultModel ModelMap containing three-stream output dataflow information, it will be displayed as confirmation of creation.
     * @return Page displaying newly created three-stream output dataflow.
     */
    @RequestMapping(value = "/addThreeStreamOutputDataflow", method = RequestMethod.POST)
    public String submitThreeStreamOutputDataflowForm(@ModelAttribute("OutputDataflowModel") OutputDataflowModel modelClass, ModelMap resultModel) {
        String outputEventType = modelClass.getOutputEventType();
        String dataflowName = outputEventType + System.currentTimeMillis();
        modelClass.setDataflowName(dataflowName);
        modelClass.setQueueName(outputEventType);

        String queueParams = dataflowHelper.generateOutputDataflow(modelClass);
        boolean added = esperUserFriendlyService.addDataflow(dataflowName, queueParams);

        resultModel.addAttribute("dataflowName", dataflowName);
        if(!added) {
            resultModel.addAttribute("result", "Error creating output dataflow. Possible reasons: Bad user input or " +
                    "dataflow with this name already defined!");
            return "addThreeStreamOutputDataflowResult";
        }

        added = manageAMQPQueue(outputEventType);

        if(!added) {
            resultModel.addAttribute("result", "Error declaring dataflow. Creation of AMQP queue failed!");
        } else {
            resultModel.addAttribute("result", "Output dataflow created successfully.");
        }
        return "addThreeStreamOutputDataflowResult";
    }

    /**
     * This method is called when user chooses to delete output dataflow.
     *
     * @param dataflowName Name of dataflow to be deleted.
     *
     * @param resultModel ModelMap used to provide information to the View
     *
     * @return Web page containing form to delete output dataflow by its name.
     */
    @RequestMapping(value = "/removeOutputDataflow", method = RequestMethod.GET)
    public String removeOutputDataflowForm(@RequestParam("dataflowName")String dataflowName, ModelMap resultModel) {
        resultModel.addAttribute("dataflowName", dataflowName);
        return "removeOutputDataflow";
    }

    /**
     * This method is called when user submits output dataflow deletion form,
     * it finds output dataflow and deletes it if possible.
     *
     * @param dataflowName Name of dataflow to be deleted.
     *
     * @param resultModel ModelMap containing result of output dataflow deletion.
     * @return Web page informing user if the output dataflow was successfully deleted.
     */
    @RequestMapping(value = "/removeOutputDataflow", method = RequestMethod.POST)
    public String submitRemoveOutputDataflowForm(@RequestParam("dataflowName") String dataflowName, ModelMap resultModel) {
        resultModel.addAttribute("dataflowName", dataflowName);
        boolean removed = esperUserFriendlyService.removeOutputDataflow(dataflowName);

        if (removed) {
            resultModel.addAttribute("result", "Output dataflow with given name removed successfully.");
        } else {
            resultModel.addAttribute("result", "Output dataflow with this name was not found, or its removal failed.");
        }
        return "removeOutputDataflowResult";
    }

    /**
     * This method is used to find out if it is necessary to create new AMQP queue based on user input.
     * If RabbitMQ server already has queue with given name, this method will return true.
     * In this case it is up to user to check if said queue has correct parameters,
     * mainly binding to exchange.
     *
     * @param eventType String describing desired AMQP queue. This string is also name of event type
     *                  located within this queue and name of input dataflow handling this queue.
     *
     * @return True if RabbitMQ server has the definition of given queue. It was either already present,
     * or this method created it with correct binding to exchange defined in config file.
     * False is returned if creation of AMQP queue failed.
     */
    private boolean manageAMQPQueue(String eventType) {
        List<String> allQueues = this.rabbitMqService.listQueues();
        boolean added = true;
        if (!allQueues.contains(eventType)) {
            added = this.rabbitMqService.createQueue(eventType);
        }
        return added;
    }
}
