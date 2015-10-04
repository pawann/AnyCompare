package org.pnwg.tools.diff;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.pnwg.tools.diff.context.FieldFeature;
import org.pnwg.tools.diff.context.IContext;
import org.pnwg.tools.diff.handler.SimpleTypeHandler;
import org.pnwg.tools.diff.model.Diff;
import org.pnwg.tools.test.data.util.DataUtil;
import org.pnwg.tools.test.model.Person;

@RunWith(BlockJUnit4ClassRunner.class)
public class ObjectCompareTest {

	@Test
	public void testCompare() throws IOException {
		Person p1 = DataUtil.readDataFromFile("test/data/person1.json", Person.class);
		Person p2 = DataUtil.readDataFromFile("test/data/person2.json", Person.class);

		IContext ctx = ObjectCompare.buildContext();
		ctx.config().register(FieldFeature.IGNORE_FIELD, "org.pnwg.tools.test.model.Person.age");
		ctx.config().register(BigDecimal.class, new SimpleTypeHandler<BigDecimal>() {
			@Override
			public boolean isEqual(BigDecimal expected, BigDecimal actual) {
				return expected.compareTo(actual) == 0;
			}
		});
		ctx.config().register(FieldFeature.KEY_FIELD, "org.pnwg.tools.test.model.Person.ssn");

		boolean result = ObjectCompare.compare(p1, p2, ctx);
		int i = 0;
		for (Diff d : ctx.getDifferences()) {
			System.out.println((++i) + "  : " + d);
		}
		Assert.assertTrue(result);
	}

	/**
	 * Generation
	 * 
	 * @throws IOException
	 */
	@Test
	public void testGenerate() throws IOException {
		Person p = buildPerson();
		DataUtil.writeData(System.out, p);
	}

	private Person buildPerson() {
		Person p = new Person();
		p.setFirstName("John");
		p.setLastName("Smith");
		p.setSsn("123-45-6789");
		p.setAge(20);
		p.setGender("M");
		p.setBankBalance(BigDecimal.valueOf(1000.40));
		p.setFriends(Arrays.asList(new Person()));
		return p;
	}
}
