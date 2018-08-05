package br.com.cardapia.company.acessmap;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import br.com.cardapia.company.acessmap.util.LatLongUtils;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void lat_long_Teste() throws Exception {
        LatLng center = new LatLng(-25.488849,-49.312515);
        LatLng result = LatLongUtils.calcEndPoint(center, 40, 90);
        LatLng result2 = LatLongUtils.calcEndPoint(result, 40, 180);
        System.out.println(center.toString());
        System.out.println(result.toString());
        System.out.println(result2.toString());
        assertEquals(1, 1);
    }
}