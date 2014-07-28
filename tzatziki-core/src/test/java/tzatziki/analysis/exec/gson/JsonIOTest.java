package tzatziki.analysis.exec.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tzatziki.analysis.exec.model.FeatureExec;

import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class JsonIOTest {

    private JsonIO jsonIO;
    private Gson gson;

    @Before
    public void setUp() {
        jsonIO = new JsonIO();
        gson = jsonIO.createGson();
    }

    @Test
    public void usecase() throws Exception {
        InputStream in = getClass().getResourceAsStream("/tzatziki/analysis/exec/two-features.json");


        List<FeatureExec> featureExecs = new JsonIO().load(in);
        assertThat(featureExecs).hasSize(2);
        System.out.println("JsonLoaderTest.usecase(" +
                new GsonBuilder().setPrettyPrinting().create().toJson(featureExecs));
    }

    @Test
    @Parameters({"/samples/coffeemachine/results/exec-all.json"})
    public void readAndBack(String resource) throws Exception {
        InputStream in = getClass().getResourceAsStream(resource);
        String origin = IOUtils.toString(in, "UTF8");

        in = getClass().getResourceAsStream(resource);
        List<FeatureExec> featureExecs = jsonIO.load(in);

        StringBuilder json = toJson(gson, featureExecs);
        assertThat(trimLines(json.toString())).isEqualTo(trimLines(origin));
    }

    private static String trimLines(String json) {
        StringBuilder b = new StringBuilder();
        for(String str : json.split("\n\r?"))
            b.append(str.trim());
        return b.toString();
    }

    private static StringBuilder toJson(Gson gson, List<FeatureExec> featureExecs) {
        StringBuilder json = new StringBuilder();
        json.append("{\"features\": [\n");
        for (int i = 0; i < featureExecs.size(); i++) {
            if (i > 0)
                json.append(",\n");
            FeatureExec f = featureExecs.get(i);
            json.append(gson.toJson(f));
        }
        json.append("]\n}");
        return json;
    }
}