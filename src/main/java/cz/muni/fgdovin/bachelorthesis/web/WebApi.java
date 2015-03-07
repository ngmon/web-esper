package cz.muni.fgdovin.bachelorthesis.web;

import com.espertech.esper.client.dataflow.EPDataFlowInstance;
import cz.muni.fgdovin.bachelorthesis.core.EsperService;
import cz.muni.fgdovin.bachelorthesis.support.EPLHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Filip Gdovin on 4. 3. 2015.
 */

@Controller
@RequestMapping(value = "/")
public class WebApi {

    @Autowired
    private EsperService esperService;

    String AMQPQueueName = "AMQPIncomingStream";
    String inputQueueName = "esperQueue";
    String inputExchangeName = "logs";

    String outputQueueName = "esperOutputQueue";
    String outputExchangeName = "sortedLogs";

    String eventType = "myEventType";
    static Map<String, Object> schema;
    static {
        schema = new HashMap<String, Object>();
        schema.put("hostname", String.class);
        schema.put("application", String.class);
        schema.put("level", Integer.class);
        schema.put("p.value", Integer.class);
        schema.put("p.value2", String.class);
        schema.put("type", String.class);
        schema.put("priority", Integer.class);
        schema.put("timestamp", String.class);
    }

    String statementName = "myTestStat";

    String query = "select avg(p.value) from instream where p.value > 4652";

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index(Model model) {
        return new ModelAndView("index");
    }

    @RequestMapping(value="/adddataflow", method=RequestMethod.GET)
    public String addDataflowForm(Model model) {
        model.addAttribute("dataflowName", new String());
        model.addAttribute("eventType", new String());
        model.addAttribute("queueName", new String());
        model.addAttribute("exchangeName", new String());
        return "dataflow";
    }

    @RequestMapping(value = "/adddataflow", method=RequestMethod.POST)
    public String addDataflowResult(@RequestParam("dataflowName") String dataflowName,
                              @RequestParam("eventType") String eventType,
                              @RequestParam("queueName") String queueName,
                              @RequestParam("exchangeName") String exchangeName,
                              ModelMap model) {
        String dataflow = EPLHelper.createAMQP(dataflowName, eventType, queueName, exchangeName);
        EPDataFlowInstance result = esperService.addDataflow(dataflowName, dataflow);
        model.addAttribute("dataflow", esperService.showDataflow(dataflowName));
        return "dataflowResult";
    }

    @RequestMapping(value = "/addschema")
    @ResponseBody
    public String addSchema() {
        esperService.addSchema(eventType, schema);
        return esperService.showSchema(eventType);
    }

    @RequestMapping(value = "/addinputdataflow")
    @ResponseBody
    public String addInputDataflow() {
        String inputQueue = EPLHelper.createAMQP(AMQPQueueName, eventType,
                inputQueueName, inputExchangeName);
        EPDataFlowInstance result = esperService.addDataflow(AMQPQueueName, inputQueue);
        if(result == null) {
            return "Already defined!";
        }
        return result.getDataFlowName();
    }

    @RequestMapping(value = "/addoutputdataflow")
    @ResponseBody
    public String addOutputDataflow() {
        String outputQueue = EPLHelper.createStatement(statementName, eventType, query,
                outputQueueName, outputExchangeName);
        EPDataFlowInstance result = esperService.addDataflow(statementName, outputQueue);
        if(result == null) {
            return "Already defined!";
        }
        return result.getDataFlowName();
    }

    @RequestMapping(value = "/removedataflow")
    @ResponseBody
    public String removeDataflow() {
        if(esperService.removeDataflow(statementName)) {
            return "Successfully removed dataflow " + statementName;
        }
        return "No dataflow with name \"" + statementName + "\" present.";
    }

    @RequestMapping(value = "/alldataflows")
    @ResponseBody
    public List<String> showDataflows() {
        List<String> result = esperService.showDataflows();
        if(result == null) {
            result = new ArrayList<String>();
            result.add("No dataflows defined!");
            return result;
        }
        return result;
    }
}
