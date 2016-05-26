/*
 * Copyright 2015 Riccardo Tasso
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.raymanrt.orientqb.query;

import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.github.raymanrt.orientqb.query.Projection.projection;
import static com.github.raymanrt.orientqb.query.ProjectionFunction.both;
import static com.github.raymanrt.orientqb.query.ProjectionFunction.in;
import static com.github.raymanrt.orientqb.query.ProjectionFunction.max;
import static com.github.raymanrt.orientqb.query.ProjectionFunction.out;
import static com.github.raymanrt.orientqb.query.Variable.thisRecord;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ProjectionMethodTest {

	@Test
	public void indexTest() {
		String simpleField = "clause";
		Projection p = projection(simpleField).index(5);
		assertEquals("clause[5]", p.toString());
		assertEquals("clause", p.getName());

		p = projection(simpleField).indexRange(5, 10);
		assertEquals("clause[5-10]", p.toString());

		p = projection(simpleField).index(5, 10);
		assertEquals("clause[5, 10]", p.toString());

		p = projection(simpleField).index(5, 10, 15);
		assertEquals("clause[5, 10, 15]", p.toString());
	}

	@Test
	public void fieldTest() {
		String simpleField = "clause";
		Projection p = projection(simpleField).field("f1");
		assertEquals("clause[f1]", p.toString());
		assertEquals("clause", p.getName());

		p = projection(simpleField).field("f1", "f2", "f3");
		assertEquals("clause[f1, f2, f3]", p.toString());
	}

    @Test
    public void projectionFieldWithExpressionTest() {
        Clause c = Projection.projection("f").eq(5);
        Projection p = Projection.projection("field").field(c);

        assertEquals("field[f = 5]", p.toString());

        c = Projection.projection("f").plus(1).gt(0);
        p = Projection.projection("field").field(c);

        assertEquals("field[f + 1 > 0]", p.toString());
    }

	@Test
	public void appendTest() {
		String simpleField = "clause";
		Projection p = projection(simpleField).append("suffix");
		assertEquals("clause.append('suffix')", p.toString());

		p = projection(simpleField).append(max(out().index(0).field("name")));
		assertEquals("clause.append(max(out()[0][name]))", p.toString());

		p = projection(simpleField).append("'''");
		assertEquals("clause.append('\'\'\'')", p.toString());
	}

	@Test
	public void asBooleanTest() {
		String simpleField = "field";
		Projection p = projection(simpleField).asBoolean();
		assertEquals("field.asBoolean()", p.toString());
	}

	@Test
	public void asDateTest() {
		String simpleField = "field";
		Projection p = projection(simpleField).asDate();
		assertEquals("field.asDate()", p.toString());
	}

	@Test
	public void asDateTimeTest() {
		String simpleField = "field";
		Projection p = projection(simpleField).asDateTime();
		assertEquals("field.asDateTime()", p.toString());
	}

	@Test
	public void asDecimalTest() {
		String simpleField = "field";
		Projection p = projection(simpleField).asDecimal();
		assertEquals("field.asDecimal()", p.toString());
	}

	@Test
	public void asFloatTest() {
		String simpleField = "field";
		Projection p = projection(simpleField).asFloat();
		assertEquals("field.asFloat()", p.toString());
	}

	@Test
	public void asIntegerTest() {
		String simpleField = "field";
		Projection p = projection(simpleField).asInteger();
		assertEquals("field.asInteger()", p.toString());
	}

	@Test
	public void asListTest() {
		String simpleField = "field";
		Projection p = projection(simpleField).asList();
		assertEquals("field.asList()", p.toString());
	}

	@Test
	public void asLongTest() {
		String simpleField = "field";
		Projection p = projection(simpleField).asLong();
		assertEquals("field.asLong()", p.toString());
	}

	@Test
	public void asMapTest() {
		String simpleField = "field";
		Projection p = projection(simpleField).asMap();
		assertEquals("field.asMap()", p.toString());
	}

	@Test
	public void asSetTest() {
		String simpleField = "field";
		Projection p = projection(simpleField).asSet();
		assertEquals("field.asSet()", p.toString());
	}

	@Test
	public void asStringTest() {
		String simpleField = "field";
		Projection p = projection(simpleField).asString();
		assertEquals("field.asString()", p.toString());
	}

	@Test
	public void bothTest() {
		String label1 = "label1";
		String label2 = "label2";

		Projection p = both(label1).both();
		assertEquals("both('label1').both()", p.toString());

		p = both(label1, label2).both(label1);
		assertEquals("both('label1', 'label2').both('label1')", p.toString());

		p = both(label1, label2).both(label1, label2);
		assertEquals("both('label1', 'label2').both('label1', 'label2')", p.toString());
	}

	@Test
	public void bothETest() {
		String label1 = "label1";
		String label2 = "label2";

		Projection p = both(label1).bothE();
		assertEquals("both('label1').bothE()", p.toString());

		p = both(label1, label2).bothE(label1);
		assertEquals("both('label1', 'label2').bothE('label1')", p.toString());

		p = both(label1, label2).bothE(label1, label2);
		assertEquals("both('label1', 'label2').bothE('label1', 'label2')", p.toString());
	}

	@Test
	public void charAtTest() {
		String simpleField = "field";
		Projection p = projection(simpleField).charAt(4);
		assertEquals("field.charAt(4)", p.toString());
	}

	@Test
	public void convertTest() {
		String simpleField = "field";
		Projection p = projection(simpleField).convert(DataType.BOOLEAN);
		assertEquals("field.convert('BOOLEAN')", p.toString());

		p = projection(simpleField).convert(DataType.DATE);
		assertEquals("field.convert('DATE')", p.toString());

		p = projection(simpleField).convert(DataType.LIST);
		assertEquals("field.convert('LIST')", p.toString());

		p = projection(simpleField).convert(DataType.STRING).append(" - ").append("suffix");
		assertEquals("field.convert('STRING').append(' - ').append('suffix')", p.toString());
	}

	@Test
	public void excludeTest() {
		String simpleField = "field";
		Projection p = projection(simpleField).exclude("a");
		assertEquals("field.exclude('a')", p.toString());

		p = projection(simpleField).exclude("a", "b", "c");
		assertEquals("field.exclude('a', 'b', 'c')", p.toString());
	}

	@Test
	public void formatTest() {
		String simpleField = "salary";
		Projection p = projection(simpleField).format("%-011d");
		assertEquals("salary.format('%-011d')", p.toString());
	}

	@Test
	public void hashTest() {
		String simpleField = "password";
		Projection p = projection(simpleField).hash();
		assertEquals("password.hash()", p.toString());

		try {
			p = projection(simpleField).hash(MessageDigest.getInstance("SHA-512"));
			assertEquals("password.hash('SHA-512')", p.toString());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void inTest() {
		String label1 = "label1";
		String label2 = "label2";

		Projection p = in(label1).in();
		assertEquals("in('label1').in()", p.toString());

		p = in(label1, label2).in(label1);
		assertEquals("in('label1', 'label2').in('label1')", p.toString());

		p = in(label1, label2).in(label1, label2);
		assertEquals("in('label1', 'label2').in('label1', 'label2')", p.toString());
	}

	@Test
	public void inETest() {
		String label1 = "label1";
		String label2 = "label2";

		Projection p = in(label1).inE();
		assertEquals("in('label1').inE()", p.toString());

		p = in(label1, label2).inE(label1);
		assertEquals("in('label1', 'label2').inE('label1')", p.toString());

		p = in(label1, label2).inE(label1, label2);
		assertEquals("in('label1', 'label2').inE('label1', 'label2')", p.toString());
	}

	@Test
	public void inVTest() {
		String label1 = "label1";
		String label2 = "label2";

		Projection p = in(label1).inV();
		assertEquals("in('label1').inV()", p.toString());

		p = in(label1, label2).inV(label1);
		assertEquals("in('label1', 'label2').inV('label1')", p.toString());

		p = in(label1, label2).inV(label1, label2);
		assertEquals("in('label1', 'label2').inV('label1', 'label2')", p.toString());
	}

	@Test
	public void includeTest() {
		String simpleField = "field";
		Projection p = projection(simpleField).include("a");
		assertEquals("field.include('a')", p.toString());

		p = projection(simpleField).include("a", "b", "c");
		assertEquals("field.include('a', 'b', 'c')", p.toString());
	}

	@Test
	public void indexOfTest() {
		String simpleField = "field";
		Projection p = projection(simpleField).indexOf("b'aaah");
		assertEquals("field.indexOf('b\\'aaah')", p.toString());

		p = projection(simpleField).indexOf("b'aaah", 5);
		assertEquals("field.indexOf('b\\'aaah', 5)", p.toString());
	}

	@Test
	public void javaTypeTest() {
		String simpleField = "field";
		Projection p = projection(simpleField).javaType();
		assertEquals("field.javaType()", p.toString());
	}

	@Test
	public void keysTest() {
		String simpleField = "field";
		Projection p = projection(simpleField).keys();
		assertEquals("field.keys()", p.toString());
	}

	@Test
	public void leftTest() {
		String simpleField = "field";
		Projection p = projection(simpleField).left(1);
		assertEquals("field.left(1)", p.toString());
	}

	@Test
	public void lengthTest() {
		String simpleField = "field";
		Projection p = projection(simpleField).length();
		assertEquals("field.length()", p.toString());
	}

	@Test
	public void normalizeTest() {
		String simpleField = "field";
		Projection p = projection(simpleField).normalize();
		assertEquals("field.normalize()", p.toString());

		p = projection(simpleField).normalize("NFD");
		assertEquals("field.normalize('NFD')", p.toString());

		p = projection(simpleField).normalize("NFD", "xxx");
		assertEquals("field.normalize('NFD', 'xxx')", p.toString());
	}

	@Test
	public void outTest() {
		String label1 = "label1";
		String label2 = "label2";

		Projection p = in(label1).out();
		assertEquals("in('label1').out()", p.toString());

		p = in(label1, label2).out(label1);
		assertEquals("in('label1', 'label2').out('label1')", p.toString());

		p = in(label1, label2).out(label1, label2);
		assertEquals("in('label1', 'label2').out('label1', 'label2')", p.toString());
	}

	@Test
	public void outETest() {
		String label1 = "label1";
		String label2 = "label2";

		Projection p = in(label1).outE();
		assertEquals("in('label1').outE()", p.toString());

		p = in(label1, label2).outE(label1);
		assertEquals("in('label1', 'label2').outE('label1')", p.toString());

		p = in(label1, label2).outE(label1, label2);
		assertEquals("in('label1', 'label2').outE('label1', 'label2')", p.toString());
	}

	@Test
	public void outVTest() {
		String label1 = "label1";
		String label2 = "label2";

		Projection p = in(label1).outV();
		assertEquals("in('label1').outV()", p.toString());

		p = in(label1, label2).outV(label1);
		assertEquals("in('label1', 'label2').outV('label1')", p.toString());

		p = in(label1, label2).outV(label1, label2);
		assertEquals("in('label1', 'label2').outV('label1', 'label2')", p.toString());
	}

	@Test
	public void prefixTest() {
		String simpleField = "clause";
		Projection p = projection(simpleField).prefix("prefix");
		assertEquals("clause.prefix('prefix')", p.toString());

		p = projection(simpleField).prefix(max(out().index(0).field("name")));
		assertEquals("clause.prefix(max(out()[0][name]))", p.toString());

		p = projection(simpleField).prefix("'''");
		assertEquals("clause.prefix('\'\'\'')", p.toString());
	}

	@Test
	public void removeTest() {
		Projection p = out().dot(in()).remove(thisRecord());
		assertEquals("out().in().remove(@this)", p.toString());
	}

	@Test
	public void removeAllTest() {
		Projection p = out().dot(in()).removeAll(out("edge"));
		assertEquals("out().in().removeAll(out('edge'))", p.toString());
	}

	@Test
	public void replaceTest() {
		Projection p = projection("field").replace("hell'o", "w'orld");
		assertEquals("field.replace('hell\\'o', 'w\\'orld')", p.toString());
	}

	@Test
	public void rightTest() {
		String simpleField = "field";
		Projection p = projection(simpleField).right(10);
		assertEquals("field.right(10)", p.toString());
	}

	@Test
	public void sizeTest() {
		String simpleField = "field";
		Projection p = projection(simpleField).size();
		assertEquals("field.size()", p.toString());
	}

	@Test
	public void subStringTest() {
		String simpleField = "field";
		Projection p = projection(simpleField).subString(0);
		assertEquals("field.subString(0)", p.toString());

		p = projection(simpleField).subString(0, 10);
		assertEquals("field.subString(0, 10)", p.toString());
	}

	@Test
	public void trimTest() {
		String simpleField = "field";
		Projection p = projection(simpleField).trim();
		assertEquals("field.trim()", p.toString());
	}

	@Test
	/**
	 * @see FetchingStrategyTest for more tests on FetchingStrategy use
	 */
	public void toJsonTest() {
		String simpleField = "field";
		Projection p = projection(simpleField).toJson();
		assertEquals("field.toJson()", p.toString());

		p = projection(simpleField).toJson(JsonFormat.TYPE);
		assertEquals("field.toJson('type')", p.toString());

		p = projection(simpleField).toJson(JsonFormat.VERSION, JsonFormat.RID);
		assertEquals("field.toJson('version,rid')", p.toString());

	}

	@Test
	public void toLowerCaseTest() {
		String simpleField = "field";
		Projection p = projection(simpleField).toLowerCase();
		assertEquals("field.toLowerCase()", p.toString());
	}

	@Test
	public void toUpperCaseTest() {
		String simpleField = "field";
		Projection p = projection(simpleField).toUpperCase();
		assertEquals("field.toUpperCase()", p.toString());
	}

	@Test
	public void typeTest() {
		String simpleField = "field";
		Projection p = projection(simpleField).type();
		assertEquals("field.type()", p.toString());
	}

	@Test
	public void valuesTest() {
		String simpleField = "field";
		Projection p = projection(simpleField).values();
		assertEquals("field.values()", p.toString());
	}

}
