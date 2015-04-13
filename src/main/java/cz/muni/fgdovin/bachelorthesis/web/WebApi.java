package cz.muni.fgdovin.bachelorthesis.web;

import cz.muni.fgdovin.bachelorthesis.core.EsperUserFriendlyService;
import cz.muni.fgdovin.bachelorthesis.support.DataflowHelper;
import cz.muni.fgdovin.bachelorthesis.support.EventTypeHelper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
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

@Controller
@RequestMapping(value = "/")
public class WebApi {

    private static final Logger logger = LogManager.getLogger("WebApi");

    @Autowired
    private EsperUserFriendlyService esperService;

    @Autowired
    private DataflowHelper dataflowHelper;

    @Autowired
    private EventTypeHelper eventTypeHelper;

    /**
     * This method is bound to "/" mapping and displays welcome page.
     *
     * @return Welcome page with available commands.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    /**
     * This method is bound to "/manageEventTypes" mapping and displays possible event types options.
     *
     * @return Page with available commands.
     */
    @RequestMapping(value = "/manageEventTypes", method = RequestMethod.GET)
    public ModelAndView manageEventTypes() {
        return new ModelAndView("manageEventTypes");
    }

    /**
     * This method is bound to "/manageInputDataflows" mapping and displays possible input dataflows options.
     *
     * @return Page with available commands.
     */
    @RequestMapping(value = "/manageInputDataflows", method = RequestMethod.GET)
    public ModelAndView manageInputDataflows() {
        return new ModelAndView("manageInputDataflows");
    }

    /**
     * This method is bound to "/manageOutputDataflows" mapping and displays possible output dataflows options.
     *
     * @return Page with available commands.
     */
    @RequestMapping(value = "/manageOutputDataflows", method = RequestMethod.GET)
    public ModelAndView manageOutputDataflows() {
        return new ModelAndView("manageOutputDataflows");
    }

    /**
     * This method is called when user decides to add new event type.
     *
     * @return Web page containing form for event type creation.
     */
    @RequestMapping(value = "/addEventType", method = RequestMethod.GET)
    public ModelAndView EventTypeForm() {
        return new ModelAndView("addEventType", "EventTypeModel", new EventTypeModel());
    }

    /**
     * This method is called when user submits a form, it POSTs form contents
     * so that new event type can be created.
     *
     * @param modelClass Model class representing fields in form, form contents are mapped to instance of this class.
     * @param resultModel ModelMap containing event type information, it will be displayed as confirmation of creation.
     * @return Page displaying newly created event type.
     */
    @RequestMapping(value = "/addEventType", method = RequestMethod.POST)
    public String submitEventTypeForm(@ModelAttribute("EventTypeModel") EventTypeModel modelClass, ModelMap resultModel) {
        Map<String, Object> properties = eventTypeHelper.toMap(modelClass.getProperties());
        boolean added = esperService.addEventType(modelClass.getEventType(), properties);
        resultModel.addAttribute("eventType", modelClass.getEventType());
        if(added) {
            resultModel.addAttribute("result", "Event type created successfully.");
        } else {
            resultModel.addAttribute("result", "Error creating event type. Bad input or is already defined!");
        }
        return "addEventTypeResult";
    }

    /**
     * This method is called when user chooses to delete event type.
     *
     * @return Web page containing form to delete event type by its name.
     */
    @RequestMapping(value = "/removeEventType", method = RequestMethod.GET)
    public ModelAndView removeEventTypeForm() {
        return new ModelAndView("removeEventType", "EventTypeModel", new EventTypeModel());
    }

    /**
     * This method is called when user submits event type deletion form, it finds event type
     * and deletes it if possible.
     *
     * @param modelClass Model class used to get user input from form.
     * @param resultModel ModelMap containing result of event type deletion.
     * @return Web page informing user if the event type was successfully deleted.
     */
    @RequestMapping(value = "/removeEventType", method = RequestMethod.POST)
    public String submitRemoveEventTypeForm(@ModelAttribute("EventTypeModel") EventTypeModel modelClass, ModelMap resultModel) {
        resultModel.addAttribute("eventType", modelClass.getEventType());
        boolean removed = esperService.removeEventType(modelClass.getEventType());
        if (removed) {
            resultModel.addAttribute("result", "No statements rely on this event type, event type removed successfully.");
        } else {
            resultModel.addAttribute("result", "Event type with this name was not found, or is still in use and was not removed.");
        }
        return "removeEventTypeResult";
    }

    @RequestMapping(value = "/showEventTypes", method = RequestMethod.GET)
    public ModelAndView showAllEventTypes() {

        List<String> list = esperService.showEventTypes();

        ModelAndView model = new ModelAndView("showEventTypes");
        model.addObject("allEventTypes", list);

        return model;
    }

    /**
     * This method is called when user decides to add new input dataflow (AMQP Source).
     *
     * @return Web page containing form for input dataflow creation.
     */
    @RequestMapping(value = "/addInputDataflow", method = RequestMethod.GET)
    public ModelAndView inputDataflowForm() {
        return new ModelAndView("addInputDataflow", "InputDataflowModel", new InputDataflowModel());
    }

    /**
     * This method is called when user submits new input dataflow form, it POSTs form contents
     * so that new input dataflow can be created.
     *
     * @param modelClass Model class representing fields in form, form contents are mapped to instance of this class.
     * @param resultModel ModelMap containing input dataflow information, it will be displayed as confirmation of creation.
     * @return Page displaying newly created input dataflow.
     */
    @RequestMapping(value = "/addInputDataflow", method = RequestMethod.POST)
    public String submitInputDataflowForm(@ModelAttribute("InputDataflowModel") InputDataflowModel modelClass, ModelMap resultModel) {
        String dataflowName = modelClass.getDataflowName();
        String queueParams = dataflowHelper.generateInputDataflow(modelClass);
        boolean added = esperService.addDataflow(dataflowName, queueParams);

        resultModel.addAttribute("dataflowName", dataflowName);
        if(added) {
            resultModel.addAttribute("result", "Input dataflow created successfully.");
        } else {
            resultModel.addAttribute("result", "Error creating input dataflow. Possible reasons: Bad user input, " +
                    "dataflow with this name already defined or given event type does not exist in Esper engine!");
        }
        return "addInputDataflowResult";
    }

    /**
     * This method is called when user chooses to delete input dataflow.
     *
     * @return Web page containing form to delete input dataflow by its name.
     */
    @RequestMapping(value = "/removeInputDataflow", method = RequestMethod.GET)
    public ModelAndView removeInputDataflowForm() {
        return new ModelAndView("removeInputDataflow", "InputDataflowModel", new InputDataflowModel());
    }

    /**
     * This method is called when user submits input dataflow deletion form,
     * it finds input dataflow and deletes it if possible.
     *
     * @param modelClass Model class used to get user input from form.
     * @param resultModel ModelMap containing result of input dataflow deletion.
     * @return Web page informing user if the input dataflow was successfully deleted.
     */
    @RequestMapping(value = "/removeInputDataflow", method = RequestMethod.POST)
    public String submitRemoveInputDataflowForm(@ModelAttribute("InputDataflowModel") InputDataflowModel modelClass, ModelMap resultModel) {
        boolean removed = esperService.removeInputDataflow(modelClass.getDataflowName());

        resultModel.addAttribute("dataflowName", modelClass.getDataflowName());
        if (removed) {
            resultModel.addAttribute("result", "Dataflow with given name removed successfully.");
        } else {
            resultModel.addAttribute("result", "Dataflow with this name was not found, or its removal failed.");
        }
        return "removeInputDataflowResult";
    }

    @RequestMapping(value = "/showInputDataflows", method = RequestMethod.GET)
    public ModelAndView showAllInputDataflows() {

        List<String> list = esperService.showInputDataflows();

        ModelAndView model = new ModelAndView("showInputDataflows");
        model.addObject("allInputDataflows", list);

        return model;
    }

    /**
     * This method is called when user decides to add new one-stream output dataflow.
     *
     * @return Web page containing form for one-stream output dataflow creation.
     */
    @RequestMapping(value = "/addOneStreamOutputDataflow", method = RequestMethod.GET)
    public ModelAndView oneStreamOutputDataflowForm() {
        return new ModelAndView("addOneStreamOutputDataflow", "OutputDataflowModel", new OutputDataflowModel());
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
        String dataflowName = modelClass.getDataflowName();
        String queueParams = dataflowHelper.generateOutputDataflow(modelClass);
        boolean added = esperService.addDataflow(dataflowName, queueParams);

        resultModel.addAttribute("dataflowName", dataflowName);
        if(added) {
            resultModel.addAttribute("result", "Output dataflow created successfully.");
        } else {
            resultModel.addAttribute("result", "Error creating output dataflow. Possible reasons: Bad user input, " +
                    "dataflow with this name already defined or given event type does not exist in Esper engine!");
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
        return new ModelAndView("addTwoStreamOutputDataflow", "OutputDataflowModel", new OutputDataflowModel());
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
        String dataflowName = modelClass.getDataflowName();
        String queueParams = dataflowHelper.generateOutputDataflow(modelClass);
        boolean added = esperService.addDataflow(dataflowName, queueParams);

        resultModel.addAttribute("dataflowName", dataflowName);
        if(added) {
            resultModel.addAttribute("result", "Output dataflow created successfully.");
        } else {
            resultModel.addAttribute("result", "Error creating output dataflow. Possible reasons: Bad user input," +
                    "dataflow with this name already defined or given event type does not exist in Esper engine!");
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
        return new ModelAndView("addThreeStreamOutputDataflow", "OutputDataflowModel", new OutputDataflowModel());
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
        String dataflowName = modelClass.getDataflowName();
        String queueParams = dataflowHelper.generateOutputDataflow(modelClass);
        boolean added = esperService.addDataflow(dataflowName, queueParams);

        resultModel.addAttribute("dataflowName", dataflowName);
        if(added) {
            resultModel.addAttribute("result", "Output dataflow created successfully.");
        } else {
            resultModel.addAttribute("result", "Error creating output dataflow. Possible reasons: Bad user input," +
                    "dataflow with this name already defined or given event type does not exist in Esper engine!");
        }
        return "addThreeStreamOutputDataflowResult";
    }

    /**
     * This method is called when user chooses to delete output dataflow.
     *
     * @return Web page containing form to delete output dataflow by its name.
     */
    @RequestMapping(value = "/removeOutputDataflow", method = RequestMethod.GET)
    public ModelAndView removeOutputDataflowForm() {
        return new ModelAndView("removeOutputDataflow", "OutputDataflowModel", new OutputDataflowModel());
    }

    /**
     * This method is called when user submits output dataflow deletion form,
     * it finds output dataflow and deletes it if possible.
     *
     * @param modelClass Model class used to get user input from form.
     * @param resultModel ModelMap containing result of output dataflow deletion.
     * @return Web page informing user if the output dataflow was successfully deleted.
     */
    @RequestMapping(value = "/removeOutputDataflow", method = RequestMethod.POST)
    public String submitRemoveOutputDataflowForm(@ModelAttribute("OutputDataflowModel") OutputDataflowModel modelClass, ModelMap resultModel) {
        resultModel.addAttribute("dataflowName", modelClass.getDataflowName());
        if (esperService.removeOutputDataflow(modelClass.getDataflowName())) {
            resultModel.addAttribute("result", "Output dataflow with given name removed successfully.");
        } else {

            resultModel.addAttribute("result", "Output dataflow with this name was not found, or its removal failed.");
        }
        return "removeOutputDataflowResult";
    }

    @RequestMapping(value = "/showOutputDataflows", method = RequestMethod.GET)
    public ModelAndView showAllOutputDataflow() {

        List<String> list = esperService.showOutputDataflows();

        ModelAndView model = new ModelAndView("showOutputDataflows");
        model.addObject("allOutputDataflows", list);

        return model;
    }
}
