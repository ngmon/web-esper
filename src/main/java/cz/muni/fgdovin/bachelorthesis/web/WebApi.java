package cz.muni.fgdovin.bachelorthesis.web;

import cz.muni.fgdovin.bachelorthesis.esper.EsperUserFriendlyService;
import cz.muni.fgdovin.bachelorthesis.rabbit.RabbitMqService;
import cz.muni.fgdovin.bachelorthesis.support.DataflowHelper;
import cz.muni.fgdovin.bachelorthesis.support.EventTypeHelper;

import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

    @Resource
    Environment environment;

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
     * This method is bound to "/manageInputDataflows" mapping and displays possible projections options.
     *
     * @return Page with available commands containing list of currently defined projections.
     */
    @RequestMapping(value = "/manageInputDataflows", method = RequestMethod.GET)
    public ModelAndView manageInputDataflows() {
        List<String> list = esperUserFriendlyService.showInputDataflows();

        Map<String, String> mapOfDetails = new HashMap<>();
        for(String dataflowName : list) {
            mapOfDetails.put(dataflowName, this.esperUserFriendlyService.showDataflow(dataflowName));
        }

        ModelAndView model = new ModelAndView("manageInputDataflows");
        model.addObject("allInputDataflows", mapOfDetails);
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

        Map<String, String> mapOfDetails = new HashMap<>();
        for(String dataflowName : list) {
            mapOfDetails.put(dataflowName, this.esperUserFriendlyService.showDataflow(dataflowName));
        }

        ModelAndView model = new ModelAndView("manageOutputDataflows");
        model.addObject("allOutputDataflows", mapOfDetails);
        return model;
    }

    /**
     * This method is called when user decides to add new projection based on event type.
     *
     * @return Web page containing form for projection creation.
     */
    @RequestMapping(value = "/addInputDataflow", method = RequestMethod.GET)
    public ModelAndView InputDataflowForm() {
        ModelAndView model = new ModelAndView("addInputDataflow", "EventTypeModel", new EventTypeModel());
        model.addObject("availExchanges", this.rabbitMqService.listExchanges());
        return model;
    }

    /**
     * This method is called when user submits a form, it POSTs form contents
     * so that new event type and its projection can be created.
     *
     * @param modelClass Model class representing fields in form, form contents are mapped to instance of this class.
     * @param resultModel ModelMap containing projection information, it will be displayed as confirmation of creation.
     * @return Page displaying newly created projection.
     */
    @RequestMapping(value = "/addInputDataflow", method = RequestMethod.POST)
    public String submitInputDataflowForm(@ModelAttribute("EventTypeModel") EventTypeModel modelClass, ModelMap resultModel) {
        String dataflowName = modelClass.getEventType();
        String exchangeName = modelClass.getExchange();
        Map<String, Object> properties = this.eventTypeHelper.toMap(modelClass.getMapProperties());

        boolean added = esperUserFriendlyService.addEventType(dataflowName, properties);
        resultModel.addAttribute("dataflowName", dataflowName);

        if(!added) {
            resultModel.addAttribute("result", "Error creating projection. Bad input or is already defined!");
            return "addInputDataflowResult";
        }

        added = checkCorrectInputQueueBinding(dataflowName, exchangeName);

        if(!added) {
            resultModel.addAttribute("result", "Error declaring projection. Creation of AMQP queue failed!");
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
            resultModel.addAttribute("result", "Projection created successfully.");
        } else {
            resultModel.addAttribute("result", "Error creating projection. Possible reasons: Bad user input or " +
                    "projection with this name already defined!");
        }

        return "addInputDataflowResult";
    }

    /**
     * This method is called when user chooses to delete projection along with its event type.
     *
     * @param dataflowName Name of projection to be deleted.
     *
     * @param resultModel ModelMap used to provide information to the View
     *
     * @return Web page containing form to delete projection by its name.
     */
    @RequestMapping(value = "/removeInputDataflow", method = RequestMethod.GET)
    public String removeInputDataflowForm(@RequestParam("dataflowName")String dataflowName, ModelMap resultModel) {
        resultModel.addAttribute("dataflowName", dataflowName);
        return "removeInputDataflow";
    }

    /**
     * This method is called when user submits projection deletion form, it finds projection
     * and deletes it if possible.
     *
     * @param dataflowName Name of projection to be deleted.
     *
     * @param resultModel ModelMap containing result of projection deletion.
     * @return Web page informing user if the projection was successfully deleted.
     */
    @RequestMapping(value = "/removeInputDataflow", method = RequestMethod.POST)
    public String submitRemoveInputDataflowForm(@RequestParam("dataflowName")String dataflowName, ModelMap resultModel) {
        resultModel.addAttribute("dataflowName", dataflowName);
        boolean removed = esperUserFriendlyService.removeEventType(dataflowName);

        if (!removed) {
            resultModel.addAttribute("result", "Projection with this name was not found, or is still in use and was not removed.");
            return "removeInputDataflowResult";
        }

        removed = esperUserFriendlyService.removeInputDataflow(dataflowName);

        if (removed) {
            resultModel.addAttribute("result", "Projection removed successfully.");
        } else {
            resultModel.addAttribute("result", "Projection removal failed, its event type removed to avoid undefined behaviour.");
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
        String dataflowName = hashWithCurrTime(outputEventType);
        modelClass.setDataflowName(dataflowName);
        modelClass.setQueueName(outputEventType);

        boolean added = checkCorrectOutputQueueBinding(outputEventType);
        if(!added) {
            resultModel.addAttribute("result", "Error declaring dataflow. Creation of AMQP queue failed!");
            return "addOneStreamOutputDataflowResult";
        }

        String queueParams = dataflowHelper.generateOutputDataflow(modelClass);
        added = esperUserFriendlyService.addDataflow(dataflowName, queueParams);

        resultModel.addAttribute("dataflowName", dataflowName);
        if(!added) {
            resultModel.addAttribute("result", "Error creating output dataflow. Possible reasons: Bad user input or " +
                    "dataflow with this name already defined!");
        } else {
            resultModel.addAttribute("result", "Output dataflow created successfully!");
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
        String dataflowName = hashWithCurrTime(outputEventType);
        modelClass.setDataflowName(dataflowName);
        modelClass.setQueueName(outputEventType);

        boolean added = checkCorrectOutputQueueBinding(outputEventType);
        if(!added) {
            resultModel.addAttribute("result", "Error declaring dataflow. Creation of AMQP queue failed!");
            return "addOneStreamOutputDataflowResult";
        }

        String queueParams = dataflowHelper.generateOutputDataflow(modelClass);
        added = esperUserFriendlyService.addDataflow(dataflowName, queueParams);

        resultModel.addAttribute("dataflowName", dataflowName);
        if(!added) {
            resultModel.addAttribute("result", "Error creating output dataflow. Possible reasons: Bad user input or " +
                    "dataflow with this name already defined!");
        } else {
            resultModel.addAttribute("result", "Output dataflow created successfully!");
        }
        return "addOneStreamOutputDataflowResult";
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
        String dataflowName = hashWithCurrTime(outputEventType);
        modelClass.setDataflowName(dataflowName);
        modelClass.setQueueName(outputEventType);

        boolean added = checkCorrectOutputQueueBinding(outputEventType);
        if(!added) {
            resultModel.addAttribute("result", "Error declaring dataflow. Creation of AMQP queue failed!");
            return "addOneStreamOutputDataflowResult";
        }

        String queueParams = dataflowHelper.generateOutputDataflow(modelClass);
        added = esperUserFriendlyService.addDataflow(dataflowName, queueParams);

        resultModel.addAttribute("dataflowName", dataflowName);
        if(!added) {
            resultModel.addAttribute("result", "Error creating output dataflow. Possible reasons: Bad user input or " +
                    "dataflow with this name already defined!");
        } else {
            resultModel.addAttribute("result", "Output dataflow created successfully!");
        }
        return "addOneStreamOutputDataflowResult";
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
     * mainly binding to output exchange saved in properties file..
     *
     * @param eventType String describing desired AMQP queue. This serves as routing key.
     *
     * @return True if RabbitMQ server has the definition of given queue bound to output exchange.
     * It was either already present, or this method created it with correct binding.
     * False is returned if creation of AMQP queue failed.
     */
    private boolean checkCorrectOutputQueueBinding(String eventType) {
        List<String> allQueues = this.rabbitMqService.listQueues();
        boolean added = true;
        if (!allQueues.contains(eventType)) {
            added = this.rabbitMqService.createQueue(eventType);
        }
        return added;
    }

    /**
     * This method is used to find out if it is necessary to create new AMQP queue based on user input.
     * If RabbitMQ server already has queue with given name, this method will return true.
     * In this case it is up to user to check if said queue has correct parameters,
     * mainly binding to exchange.
     *
     * @param eventType String describing desired AMQP queue. This string is also name of event type
     *                  located within this queue and name of projection handling this queue.
     *
     * @param exchangeName String containing name of exchange, to which queue should be bound.
     *                     This is different than binding to implicitly saved output one.
     *
     * @return True if RabbitMQ server has the definition of given queue. It was either already present,
     * or this method created it with correct binding to exchange defined in config file.
     * False is returned if creation of AMQP queue failed.
     */
    private boolean checkCorrectInputQueueBinding(String eventType, String exchangeName) {
        List<String> allQueues = this.rabbitMqService.listQueues(exchangeName);
        boolean added = true;
        if (!allQueues.contains(eventType)) {
            added = this.rabbitMqService.createQueue(eventType, exchangeName);
        }
        return added;
    }

    private String hashWithCurrTime(String outputEventType) {
        String characters = outputEventType + System.currentTimeMillis();
        Random rng = new Random(System.currentTimeMillis());

        char[] text = new char[12];
        for (int i = 0; i < 12; i++)
        {
            text[i] = characters.charAt(rng.nextInt(characters.length()));

            while(Character.isDigit(text[0])) {
                text[0] = characters.charAt(rng.nextInt(characters.length()));
            }
        }
        return new String(text);
    }

}
