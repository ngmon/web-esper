package cz.muni.fgdovin.bachelorthesis.web;

import cz.muni.fgdovin.bachelorthesis.core.EsperService;
import cz.muni.fgdovin.bachelorthesis.support.EPLHelper;
import cz.muni.fgdovin.bachelorthesis.support.SchemaHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
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
    private EsperService esperService;

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
        return new ModelAndView("addDataflow", "EPLHelper", new EPLHelper());
    }

    /**
     * This method is called when user completes a form, it POSTs form contents
     * so that new dataflow can be created.
     *
     * @param EPLHelper Model class representing fields in form, form contents are mapped to instance of this class.
     * @param model ModelMap containing dataflow information, it will be displayed as confirmation of creation.
     * @return Page displaying newly created dataflow.
     */
    @RequestMapping(value = "/addDataflow", method = RequestMethod.POST)
    public String submitDataflowForm(@Valid @ModelAttribute("EPLHelper")EPLHelper EPLHelper, ModelMap model) {
        String dataflowName = EPLHelper.getDataflowName();
        String eventType = EPLHelper.getEventType();
        String query = EPLHelper.getQuery();
        String queueName = EPLHelper.getQueueName();
        String exchangeName = EPLHelper.getExchangeName();

        model.addAttribute("dataflowName", dataflowName);
        model.addAttribute("eventType", eventType);
        model.addAttribute("query", query);
        model.addAttribute("queueName", queueName);
        model.addAttribute("exchangeName", exchangeName);

        String queueParams = EPLHelper.toString(dataflowName, eventType, query, queueName, exchangeName);
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
        return new ModelAndView("removeDataflow", "EPLHelper", new EPLHelper());
    }

    /**
     * This method is called when user submits dataflow deletion form, it finds dataflow
     * and deletes it if possible.
     *
     * @param myHelper Model class used to get user input from form.
     * @param model ModelMap containing result of dataflow deletion.
     * @return Web page informing user if the dataflow was successfully deleted.
     */
    @RequestMapping(value = "/removeDataflow", method = RequestMethod.POST)
    public String submitRemoveDataflowForm(@ModelAttribute("EPLHelper")EPLHelper myHelper, ModelMap model) {
        model.addAttribute("dataflowName", myHelper.getDataflowName());
        if (esperService.removeDataflow(myHelper.getDataflowName())) {
            model.addAttribute("eventType", "Dataflow with given name removed successfully.");
        } else {

            model.addAttribute("eventType", "Dataflow with this name was not found, or its removal failed.");
        }
        return "removeDataflowResult";
    }

    /**
     * This method is called when user decides to add new event schema.
     *
     * @return Web page containing form for schema creation.
     */
    @RequestMapping(value = "/addSchema", method = RequestMethod.GET)
    public ModelAndView schemaForm() {
        return new ModelAndView("addSchema", "SchemaHelper", new SchemaHelper());
    }

    /**
     * This method is called when user completes a form, it POSTs form contents
     * so that new schema can be created.
     *
     * @param myHelper Model class representing fields in form, form contents are mapped to instance of this class.
     * @param model ModelMap containing schema information, it will be displayed as confirmation of creation.
     * @return Page displaying newly created schema.
     */
    @RequestMapping(value = "/addSchema", method = RequestMethod.POST)
    public String submitSchemaForm(@ModelAttribute("SchemaHelper")SchemaHelper myHelper, ModelMap model) {
        model.addAttribute("eventType", myHelper.getEventType());
        model.addAttribute("properties", myHelper.getProperties());
        Map<String, Object> properties = SchemaHelper.toMap(myHelper.getProperties());
        esperService.addSchema(myHelper.getEventType(), properties);
        return "addSchemaResult";
    }

    /**
     * This method is called when user chooses to delete schema.
     *
     * @return Web page containing form to delete schema by its name.
     */
    @RequestMapping(value = "/removeSchema", method = RequestMethod.GET)
    public ModelAndView removeSchemaForm() {
        return new ModelAndView("removeSchema", "SchemaHelper", new SchemaHelper());
    }

    /**
     * This method is called when user submits schema deletion form, it finds schema
     * and deletes it if possible.
     *
     * @param myHelper Model class used to get user input from form.
     * @param model ModelMap containing result of schema deletion.
     * @return Web page informing user if the schema was successfully deleted.
     */
    @RequestMapping(value = "/removeSchema", method = RequestMethod.POST)
    public String submitRemoveSchemaForm(@ModelAttribute("SchemaHelper")SchemaHelper myHelper, ModelMap model) {
        model.addAttribute("eventType", myHelper.getEventType());
        if (esperService.removeSchema(myHelper.getEventType())) {
            model.addAttribute("properties", "No statements rely on this event type, schema removed successfully.");
        } else {

            model.addAttribute("properties", "Event schema with this name was not found, or is still in use and was not removed.");
        }
        return "removeSchemaResult";
    }
}
