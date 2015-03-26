package cz.muni.fgdovin.bachelorthesis.web;

import cz.muni.fgdovin.bachelorthesis.core.EsperUserFriendlyService;
import cz.muni.fgdovin.bachelorthesis.support.DataflowHelper;
import cz.muni.fgdovin.bachelorthesis.support.EventTypeHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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

    @Autowired
    private EsperUserFriendlyService esperService;

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
     * This method is called when user decides to add new dataflow.
     *
     * @return Web page containing form for dataflow creation.
     */
    @RequestMapping(value = "/addDataflow", method = RequestMethod.GET)
    public ModelAndView dataflowForm() {
        return new ModelAndView("addDataflow", "DataflowModel", new DataflowModel());
    }

    /**
     * This method is called when user completes a form, it POSTs form contents
     * so that new dataflow can be created.
     *
     * @param modelClass Model class representing fields in form, form contents are mapped to instance of this class.
     * @param resultModel ModelMap containing dataflow information, it will be displayed as confirmation of creation.
     * @return Page displaying newly created dataflow.
     */
    @RequestMapping(value = "/addDataflow", method = RequestMethod.POST)
    public String submitDataflowForm(@ModelAttribute("DataflowModel") DataflowModel modelClass, ModelMap resultModel) {
        String dataflowName = modelClass.getDataflowName();
        String eventType = modelClass.getEventType();
        String query = modelClass.getQuery();
        String queueName = modelClass.getQueueName();
        String exchangeName = modelClass.getExchangeName();

        resultModel.addAttribute("dataflowName", dataflowName);
        resultModel.addAttribute("eventType", eventType);
        resultModel.addAttribute("query", query);
        resultModel.addAttribute("queueName", queueName);
        resultModel.addAttribute("exchangeName", exchangeName);

        String queueParams = DataflowHelper.generateEPL(modelClass);
            esperService.addDataflow(dataflowName, queueParams);

        return "addDataflowResult";
    }

    /**
     * This method is called when user chooses to delete dataflow.
     *
     * @return Web page containing form to delete dataflow by its name.
     */
    @RequestMapping(value = "/removeDataflow", method = RequestMethod.GET)
    public ModelAndView removeDataflowForm() {
        return new ModelAndView("removeDataflow", "DataflowModel", new DataflowModel());
    }

    /**
     * This method is called when user submits dataflow deletion form, it finds dataflow
     * and deletes it if possible.
     *
     * @param modelClass Model class used to get user input from form.
     * @param resultModel ModelMap containing result of dataflow deletion.
     * @return Web page informing user if the dataflow was successfully deleted.
     */
    @RequestMapping(value = "/removeDataflow", method = RequestMethod.POST)
    public String submitRemoveDataflowForm(@ModelAttribute("DataflowModel") DataflowModel modelClass, ModelMap resultModel) {
        resultModel.addAttribute("dataflowName", modelClass.getDataflowName());
        if (esperService.removeDataflow(modelClass.getDataflowName())) {
            resultModel.addAttribute("eventType", "Dataflow with given name removed successfully.");
        } else {

            resultModel.addAttribute("eventType", "Dataflow with this name was not found, or its removal failed.");
        }
        return "removeDataflowResult";
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
     * This method is called when user completes a form, it POSTs form contents
     * so that new event type can be created.
     *
     * @param modelClass Model class representing fields in form, form contents are mapped to instance of this class.
     * @param resultModel ModelMap containing event type information, it will be displayed as confirmation of creation.
     * @return Page displaying newly created event type.
     */
    @RequestMapping(value = "/addEventType", method = RequestMethod.POST)
    public String submitEventTypeForm(@ModelAttribute("EventTypeModel") EventTypeModel modelClass, ModelMap resultModel) {
        resultModel.addAttribute("eventType", modelClass.getEventType());
        resultModel.addAttribute("properties", modelClass.getProperties());
        Map<String, Object> properties = EventTypeHelper.toMap(modelClass.getProperties());
        esperService.addEventType(modelClass.getEventType(), properties);
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
        if (esperService.removeEventType(modelClass.getEventType())) {
            resultModel.addAttribute("properties", "No statements rely on this event type, event type removed successfully.");
        } else {
            resultModel.addAttribute("properties", "Event type with this name was not found, or is still in use and was not removed.");
        }
        return "removeEventTypeResult";
    }
}
