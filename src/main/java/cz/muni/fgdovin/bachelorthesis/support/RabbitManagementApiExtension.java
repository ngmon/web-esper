package cz.muni.fgdovin.bachelorthesis.support;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.nigajuan.rabbit.management.client.AuthenticationRequestInterceptor;
import org.nigajuan.rabbit.management.client.RabbitManagementApi;
import org.nigajuan.rabbit.management.client.domain.binding.queue.QueueBind;
import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.converter.JacksonConverter;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Path;

//TODO find a better way. right now copy+paste from RabbitManagementApi so that it stays intact.
public interface RabbitManagementApiExtension {
    
    @POST("/api/bindings/{vhost}/e/{source}/e/{destination}")
    Response bindExchangeToExchangeWithKey(@Path("vhost") String vhost, @Path("source") String source, @Path("destination") String destination, @Body QueueBind bind);

    static RabbitManagementApiExtension newInstance(String endpoint, String username, String password) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(endpoint)
                .setConverter(new JacksonConverter(objectMapper));

        if (username != null && password != null) {
            builder.setRequestInterceptor(new AuthenticationRequestInterceptor(username, password));
        }

        return builder.build().create(RabbitManagementApiExtension.class);
    }
}
