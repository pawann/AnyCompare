package org.pnwg.tools.diff;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.pnwg.tools.diff.context.FieldFeature;
import org.pnwg.tools.diff.context.IContext;
import org.pnwg.tools.diff.model.Diff;
import org.pnwg.tools.helpers.FieldUtil;
import org.pnwg.tools.test.data.util.DataUtil;
import org.pnwg.tools.test.model.Person;

@RunWith(BlockJUnit4ClassRunner.class)
public class ObjectCompareTest {

	/**
	 * Case 0: Identical person do not result in any differences
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCompareEqualsCase0() throws IOException {
		Person p1 = DataUtil.readDataFromFile("test/data/case0/case0-person.json", Person.class);
		Person p2 = DataUtil.readDataFromFile("test/data/case0/case0-person.json", Person.class);

		IContext ctx = ObjectCompare.compare(p1, p2);
		debug(ctx.getDifferences());
		Assert.assertFalse(ctx.hasDifferences());
	}

	/**
	 * Case 1: If fields have different value, they result in differences.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCompareCase1() throws IOException {
		Person p1 = DataUtil.readDataFromFile("test/data/case1/case1-person1.json", Person.class);
		Person p2 = DataUtil.readDataFromFile("test/data/case1/case1-person2.json", Person.class);

		IContext ctx = ObjectCompare.compare(p1, p2);
		debug(ctx.getDifferences());
		Assert.assertTrue(ctx.hasDifferences());
		Assert.assertEquals(4, ctx.getDifferences().size());
	}

	/**
	 * Case 2: One of the field is different but we can configure it to ignore
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCompareCase2() throws IOException {
		Person p1 = DataUtil.readDataFromFile("test/data/case2/case2-person1.json", Person.class);
		Person p2 = DataUtil.readDataFromFile("test/data/case2/case2-person2.json", Person.class);

		// Custom context
		String ageFieldName = FieldUtil.makeFieldName(Person.class, "age");
		IContext ctx = ObjectCompare.buildContext().register(FieldFeature.IGNORE_FIELD, ageFieldName);
		ObjectCompare.compare(p1, p2, ctx);
		debug(ctx.getDifferences());
		Assert.assertFalse(ctx.hasDifferences());
	}

	/**
	 * Case 3: Nested collection is compared. Collection is not in a order, but
	 * entities are identical.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCompareCase3() throws IOException {
		Person p1 = DataUtil.readDataFromFile("test/data/case3/case3-person1.json", Person.class);
		Person p2 = DataUtil.readDataFromFile("test/data/case3/case3-person2.json", Person.class);

		// Custom context
		IContext ctx = ObjectCompare.compare(p1, p2);
		debug(ctx.getDifferences());
		Assert.assertFalse(ctx.hasDifferences());
	}

	/**
	 * Case 4: Nested collection is compared and differences are identified.
	 * Collection is not in a order.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCompareCase4() throws IOException {
		Person p1 = DataUtil.readDataFromFile("test/data/case4/case4-person1.json", Person.class);
		Person p2 = DataUtil.readDataFromFile("test/data/case4/case4-person2.json", Person.class);

		// Custom context
		IContext ctx = ObjectCompare.compare(p1, p2);
		debug(ctx.getDifferences());
		Assert.assertTrue(ctx.hasDifferences());
	}

	/**
	 * Case 4: Nested collection is compared and differences are identified.
	 * Collection is not in a order. second level nested collection has a
	 * difference.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCompareCase5() throws IOException {
		Person p1 = DataUtil.readDataFromFile("test/data/case5/case5-person1.json", Person.class);
		Person p2 = DataUtil.readDataFromFile("test/data/case5/case5-person2.json", Person.class);

		// Custom context
		IContext ctx = ObjectCompare.compare(p1, p2);
		debug(ctx.getDifferences());
		Assert.assertTrue(ctx.hasDifferences());
	}

	/**
	 * 
	 * @param diffs
	 */

	private void debug(List<Diff> diffs) {
		System.out.println("*** Debug ****");
		int i = 0;
		for (Diff d : diffs) {
			System.out.println((++i) + "  : " + d);
		}
	}

	// ctx.config().register(FieldFeature.IGNORE_FIELD,
	// "org.pnwg.tools.test.model.Person.age");
	// ctx.config().register(FieldFeature.KEY_FIELD,
	// "org.pnwg.tools.test.model.Person.ssn");
	// //Demonstrating custom compare Handling of Bigdecimal
	// ctx.config().register(BigDecimal.class, new
	// SimpleTypeHandler<BigDecimal>() {
	// @Override
	// public boolean isEqual(BigDecimal expected, BigDecimal actual) {
	// return expected.compareTo(actual) == 0;
	// }
	// });

	/**
	 * Generation
	 * 
	 * @throws IOException
	 */
	// @Test
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
