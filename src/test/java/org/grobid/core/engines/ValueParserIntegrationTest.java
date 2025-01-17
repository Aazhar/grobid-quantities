package org.grobid.core.engines;

import org.grobid.core.data.Value;
import org.grobid.core.data.ValueBlock;
import org.grobid.core.main.LibraryLoader;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class ValueParserIntegrationTest {
    ValueParser target;

    @Before
    public void setUp() throws Exception {
        LibraryLoader.load();
        target = new ValueParser();
    }

    @Test
    public void testTagUnit_exponential_1() throws Exception {
        String input = "0.3 x 10-7";
        ValueBlock output = target.tagValue(input);

        System.out.println(input + " -> " + output);
        System.out.println(output.getRawTaggedValue());
        
        assertThat(output.getNumber().toString(), is("0.3"));
        assertThat(output.getBase().toString(), is("10"));
        assertThat(output.getPow().toString(), is("-7"));
    }

    @Ignore("model not yet mature for this test")
    @Test
    public void testTagUnit_exponential_2() throws Exception {
        String input = "10 e -1";
        ValueBlock output = target.tagValue(input);
        System.out.println(input + " -> " + output);
        System.out.println(output.getRawTaggedValue());

        assertThat(output.getNumber().toString(), is("10"));
        assertThat(output.getExp().toString(), is("-1"));
    }

    @Test
    @Ignore
    public void testParseValue_esponential_1() throws Exception {
        String input = "10 e -1";
        Value output = target.parseValue(input);
        System.out.println(input + " -> " + output);

        assertThat(output.getStructure().getNumber().toString(), is("10"));
        assertThat(output.getStructure().getExp().toString(), is("-1"));
    }

    @Test
    public void testParseValueBlock_simpleNumeric() throws Exception {
        ValueBlock block = new ValueBlock();
        block.setNumber("20");
        final BigDecimal bigDecimal = target.parseValueBlock(block, Locale.ENGLISH);

        assertThat(bigDecimal, is(not(nullValue())));
        assertThat(bigDecimal, is(new BigDecimal("20")));
    }

    @Test
    public void testParseValueBlock_simpleNumericWithBaseAndPow() throws Exception {
        ValueBlock block = new ValueBlock();
        block.setNumber("20");
        block.setPow("-1");
        block.setBase("10");
        final BigDecimal bigDecimal = target.parseValueBlock(block, Locale.ENGLISH);

        assertThat(bigDecimal, is(not(nullValue())));
        assertThat(bigDecimal.intValue(), is(2));
    }

    @Test
    public void testParseValueBlock_simpleNumericWithBase() throws Exception {
        ValueBlock block = new ValueBlock();
        block.setNumber("200");
        block.setBase("10");
        final BigDecimal bigDecimal = target.parseValueBlock(block, Locale.ENGLISH);

        assertThat(bigDecimal, is(not(nullValue())));
        assertThat(bigDecimal.intValue(), is(200));
    }

}