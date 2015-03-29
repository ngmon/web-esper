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
     * This method is bound to "/manageDataflows" mapping and displays possible dataflows options.
     *
     * @return Page with available commands.
     */
    @RequestMapping(value = "/manageDataflows", method = RequestMethod.GET)
    public ModelAndView manageDataflows() {
        return new ModelAndView("manageDataflows");
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
     * This method is bound to "/manageEPLStatements" mapping and displays possible EPL statements options.
     *
     * @return Page with available commands.
     */
    @RequestMapping(value = "/manageEPLStatements", method = RequestMethod.GET)
    public ModelAndView manageEPLStatements() {
        return new ModelAndView("manageEPLStatements");
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

    @RequestMapping(value = "/showEventTypes", method = RequestMethod.GET)
    public ModelAndView showAllEventTypes() {

        List<String> list = esperService.showEventTypes();

        ModelAndView model = new ModelAndView("showEventTypes");
        model.addObject("lists", list);

        return model;
    }

    /**
     * This method is called when user decides to add new dataflow (AMQP Source).
     *
     * @return Web page containing form for dataflow creation.
     */
    @RequestMapping(value = "/addDataflow", method = RequestMethod.GET)
    public ModelAndView dataflowForm() {
        return new ModelAndView("addDataflow", "DataflowModel", new DataflowModel());
    }

    /**
     * This method is called when user submits new dataflow form, it POSTs form contents
     * so that new dataflow can be created.
     *
     * @param modelClass Model class representing fields in form, form contents are mapped to instance of this class.
     * @param resultModel ModelMap containing dataflow information, it will be displayed as confirmation of creation.
     * @return Page displaying newly created dataflow.
     */
    @RequestMapping(value = "/addDataflow", method = RequestMethod.POST)
    public String submitDataflowForm(@ModelAttribute("DataflowModel") DataflowModel modelClass, ModelMap resultModel) {
        String dataflowName = modelClass.getDataflowName();
        String eventType = modelClass.getFirstEventType();
        String queueName = modelClass.getQueueName();
        String exchangeName = modelClass.getExchangeName();

        resultModel.addAttribute("dataflowName", dataflowName);
        resultModel.addAttribute("eventType", eventType);
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
     * This method is called when user submits dataflow deletion form,
     * it finds dataflow and deletes it if possible.
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

    @RequestMapping(value = "/showDataflows", method = RequestMethod.GET)
    public ModelAndView showAllDataflows() {

        List<String> list = esperService.showDataflows();

        ModelAndView model = new ModelAndView("showDataflows");
        model.addObject("lists", list);

        return model;
    }

    /**
     * This method is called when user decides to add new one-stream EPL statement.
     *
     * @return Web page containing form for one-stream EPL statement creation.
     */
    @RequestMapping(value = "/addOneStreamEPL", method = RequestMethod.GET)
    public ModelAndView oneStreamEPLForm() {
        return new ModelAndView("addOneStreamEPL", "DataflowModel", new DataflowModel());
    }

    /**
     * This method is called when user submitss a form, it POSTs form contents
     * so that new one-stream EPL statement can be created.
     *
     * @param modelClass Model class representing fields in form, form contents are mapped to instance of this class.
     * @param resultModel ModelMap containing one-stream EPL statement information, it will be displayed as confirmation of creation.
     * @return Page displaying newly created one-stream EPL statement.
     */
    @RequestMapping(value = "/addOneStreamEPL", method = RequestMethod.POST)
    public String submitOneStreamEPLForm(@ModelAttribute("DataflowModel") DataflowModel modelClass, ModelMap resultModel) {
        String dataflowName = modelClass.getDataflowName();
        String firstEventType = modelClass.getFirstEventType();
        String outputEventType = modelClass.getOutputEventType();
        String query = modelClass.getQuery();
        String queueName = modelClass.getQueueName();
        String exchangeName = modelClass.getExchangeName();

        resultModel.addAttribute("dataflowName", dataflowName);
        resultModel.addAttribute("firstEventType", firstEventType);
        resultModel.addAttribute("outputEventType", outputEventType);
        resultModel.addAttribute("query", query);
        resultModel.addAttribute("queueName", queueName);
        resultModel.addAttribute("exchangeName", exchangeName);

        String queueParams = DataflowHelper.generateEPL(modelClass);
        esperService.addDataflow(dataflowName, queueParams);

        return "addOneStreamEPLResult";
    }

    /**
     * This method is called when user decides to add new two-stream EPL statement.
     *
     * @return Web page containing form for two-stream EPL statement creation.
     */
    @RequestMapping(value = "/addTwoStreamEPL", method = RequestMethod.GET)
    public ModelAndView twoStreamEPLForm() {
        return new ModelAndView("addTwoStreamEPL", "DataflowModel", new DataflowModel());
    }

    /**
     * This method is called when user submits a form, it POSTs form contents
     * so that new two-stream EPL statement can be created.
     *
     * @param modelClass Model class representing fields in form, form contents are mapped to instance of this class.
     * @param resultModel ModelMap containing two-stream EPL statement information, it will be displayed as confirmation of creation.
     * @return Page displaying newly created two-stream EPL statement.
     */
    @RequestMapping(value = "/addTwoStreamEPL", method = RequestMethod.POST)
    public String submitTwoStreamEPLForm(@ModelAttribute("DataflowModel") DataflowModel modelClass, ModelMap resultModel) {
        String dataflowName = modelClass.getDataflowName();
        String firstEventType = modelClass.getFirstEventType();
        String secondEventType = modelClass.getSecondEventType();
        String outputEventType = modelClass.getOutputEventType();
        String query = modelClass.getQuery();
        String queueName = modelClass.getQueueName();
        String exchangeName = modelClass.getExchangeName();

        resultModel.addAttribute("dataflowName", dataflowName);
        resultModel.addAttribute("firstEventType", firstEventType);
        resultModel.addAttribute("secondEventType", secondEventType);
        resultModel.addAttribute("outputEventType", outputEventType);
        resultModel.addAttribute("query", query);
        resultModel.addAttribute("queueName", queueName);
        resultModel.addAttribute("exchangeName", exchangeName);

        String queueParams = DataflowHelper.generateEPL(modelClass);
        esperService.addDataflow(dataflowName, queueParams);

        return "addTwoStreamEPLResult";
    }

    /**
     * This method is called when user decides to add new three-stream EPL statement.
     *
     * @return Web page containing form for three-stream EPL statement creation.
     */
    @RequestMapping(value = "/addThreeStreamEPL", method = RequestMethod.GET)
    public ModelAndView threeStreamEPLForm() {
        return new ModelAndView("addThreeStreamEPL", "DataflowModel", new DataflowModel());
    }

    /**
     * This method is called when user submits a form, it POSTs form contents
     * so that new two-stream EPL statement can be created.
     *
     * @param modelClass Model class representing fields in form, form contents are mapped to instance of this class.
     * @param resultModel ModelMap containing three-stream EPL statement information, it will be displayed as confirmation of creation.
     * @return Page displaying newly created three-stream EPL statement.
     */
    @RequestMapping(value = "/addThreeStreamEPL", method = RequestMethod.POST)
    public String submitThreeStreamEPLForm(@ModelAttribute("DataflowModel") DataflowModel modelClass, ModelMap resultModel) {
        String dataflowName = modelClass.getDataflowName();
        String firstEventType = modelClass.getFirstEventType();
        String secondEventType = modelClass.getSecondEventType();
        String thirdEventType = modelClass.getThirdEventType();
        String outputEventType = modelClass.getOutputEventType();
        String query = modelClass.getQuery();
        String queueName = modelClass.getQueueName();
        String exchangeName = modelClass.getExchangeName();

        resultModel.addAttribute("dataflowName", dataflowName);
        resultModel.addAttribute("firstEventType", firstEventType);
        resultModel.addAttribute("secondEventType", secondEventType);
        resultModel.addAttribute("thirdEventType", thirdEventType);
        resultModel.addAttribute("outputEventType", outputEventType);
        resultModel.addAttribute("query", query);
        resultModel.addAttribute("queueName", queueName);
        resultModel.addAttribute("exchangeName", exchangeName);

        String queueParams = DataflowHelper.generateEPL(modelClass);
        esperService.addDataflow(dataflowName, queueParams);

        return "addThreeStreamEPLResult";
    }

    /**
     * This method is called when user chooses to delete EPL statement.
     *
     * @return Web page containing form to delete EPL statement by its name.
     */
    @RequestMapping(value = "/removeEPL", method = RequestMethod.GET)
    public ModelAndView removeEPLForm() {
        return new ModelAndView("removeEPL", "DataflowModel", new DataflowModel());
    }

    /**
     * This method is called when user submits EPL statement deletion form,
     * it finds EPL statement and deletes it if possible.
     *
     * @param modelClass Model class used to get user input from form.
     * @param resultModel ModelMap containing result of EPL statement deletion.
     * @return Web page informing user if the EPL statement was successfully deleted.
     */
    @RequestMapping(value = "/removeEPL", method = RequestMethod.POST)
    public String submitRemoveEPLForm(@ModelAttribute("DataflowModel") DataflowModel modelClass, ModelMap resultModel) {
        resultModel.addAttribute("dataflowName", modelClass.getDataflowName());
        if (esperService.removeDataflow(modelClass.getDataflowName())) {
            resultModel.addAttribute("eventType", "EPL statement with given name removed successfully.");
        } else {

            resultModel.addAttribute("eventType", "EPL statement with this name was not found, or its removal failed.");
        }
        return "removeEPLResult";
    }

    @RequestMapping(value = "/showEPLStatements", method = RequestMethod.GET)
    public ModelAndView showAllEPLStatements() {

        List<String> list = esperService.showEPLStatements();

        ModelAndView model = new ModelAndView("showEPLStatements");
        model.addObject("lists", list);

        return model;
    }
}
