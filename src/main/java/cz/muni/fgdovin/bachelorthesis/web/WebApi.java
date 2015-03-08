package cz.muni.fgdovin.bachelorthesis.web;

import cz.muni.fgdovin.bachelorthesis.core.EsperService;
import cz.muni.fgdovin.bachelorthesis.support.EPLHelper;

import cz.muni.fgdovin.bachelorthesis.support.SchemaHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Map;

/**
 * Created by Filip Gdovin on 4. 3. 2015.
 */

@Controller
@RequestMapping(value = "/")
public class WebApi {

    @Autowired
    private EsperService esperService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index(Model model) {
        return new ModelAndView("index");
    }

    @RequestMapping(value = "/addDataflow", method = RequestMethod.GET)
    public ModelAndView dataflowForm() {
        return new ModelAndView("addDataflow", "EPLHelper", new EPLHelper());
    }

    @RequestMapping(value = "/addDataflow", method = RequestMethod.POST)
    public String submitDataflowForm(@Valid @ModelAttribute("EPLHelper")EPLHelper EPLHelper,
                         BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return "error";
        }
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

    @RequestMapping(value = "/removeDataflow", method = RequestMethod.GET)
    public ModelAndView removeDataflowForm() {
        return new ModelAndView("removeDataflow", "EPLHelper", new EPLHelper());
    }

    @RequestMapping(value = "/removeDataflow", method = RequestMethod.POST)
    public String submitRemoveDataflowForm(@Valid @ModelAttribute("EPLHelper")EPLHelper myHelper,
                                         BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return "error";
        }
        model.addAttribute("dataflowName", myHelper.getDataflowName());
        if (esperService.removeDataflow(myHelper.getDataflowName())) {
            model.addAttribute("eventType", "Dataflow with given name removed successfully.");
        } else {

            model.addAttribute("eventType", "Dataflow with this name was not found, or its removal failed.");
        }
        return "removeDataflowResult";
    }

    @RequestMapping(value = "/addSchema", method = RequestMethod.GET)
    public ModelAndView schemaForm() {
        return new ModelAndView("addSchema", "SchemaHelper", new SchemaHelper());
    }

    @RequestMapping(value = "/addSchema", method = RequestMethod.POST)
    public String submitSchemaForm(@Valid @ModelAttribute("SchemaHelper")SchemaHelper myHelper,
                                     BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return "error";
        }
        model.addAttribute("eventType", myHelper.getEventType());
        model.addAttribute("properties", myHelper.getProperties());
        Map<String, Object> properties = SchemaHelper.toMap(myHelper.getProperties());
        esperService.addSchema(myHelper.getEventType(), properties);
        return "addSchemaResult";
    }

    @RequestMapping(value = "/removeSchema", method = RequestMethod.GET)
    public ModelAndView removeSchemaForm() {
        return new ModelAndView("removeSchema", "SchemaHelper", new SchemaHelper());
    }

    @RequestMapping(value = "/removeSchema", method = RequestMethod.POST)
    public String submitRemoveSchemaForm(@Valid @ModelAttribute("SchemaHelper")SchemaHelper myHelper,
                                   BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return "error";
        }
        model.addAttribute("eventType", myHelper.getEventType());
        if (esperService.removeSchema(myHelper.getEventType())) {
            model.addAttribute("properties", "No statements rely on this event type, schema removed successfully.");
        } else {

            model.addAttribute("properties", "Event schema with this name was not found, or is still in use and was not removed.");
        }
        return "removeSchemaResult";
    }
}
