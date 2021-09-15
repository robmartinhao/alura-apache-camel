package br.com.alura;

import com.thoughtworks.xstream.XStream;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.xstream.XStreamDataFormat;
import org.apache.camel.impl.DefaultCamelContext;

public class RotaHttpPollingNegociacoes {

    public static void main(String[] args) throws Exception {

        CamelContext context = new DefaultCamelContext();

        final XStream xstream = new XStream();
        xstream.alias("negociacao", Negociacao.class);

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                from("timer://negociacoes?fixedRate=true&delay=1s&period=360s")
                        .to("http4://argentumws-spring.herokuapp.com/negociacoes")
                        .convertBodyTo(String.class)
                        .unmarshal(new XStreamDataFormat(xstream))
                        .split(body())
                        .log("${body}")
//                        .setHeader(Exchange.FILE_NAME, constant("negociacoes.xml"))
//                        .to("file:saida");
                        .end();
            }
        });
        context.start();
        Thread.sleep(20000);
        context.stop();
    }
}
