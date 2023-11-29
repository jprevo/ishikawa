package org.sitsgo.ishikawa.goserver.ogs;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sitsgo.ishikawa.goserver.GoServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class OgsServerTests {

    @Autowired
    private OgsServer ogs;

    @Test
    public void testComputeRank() throws GoServerException, JSONException {
        assertThat(OgsServer.computeRank(2060.991855209452)).isEqualTo("2d");
        assertThat(OgsServer.computeRank(700)).isEqualTo("24k");
        assertThat(OgsServer.computeRank(1854)).isEqualTo("1k");
        assertThat(OgsServer.computeRank(1459)).isEqualTo("7k");
        assertThat(OgsServer.computeRank(1380)).isEqualTo("8k");
    }
}
