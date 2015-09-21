package sbahnmucstatustracker.db;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class DatabaseReaderTest {

	@Test
	public void testCreateQueryStatementToLoadData() {
		List<String> lines = new LinkedList<>();
		lines.add("S1");
		lines.add("S2");

		final String expectedStatement = "SELECT X.TIME, MAX(S1_PERCENT) AS S1_PERCENT, MAX(S2_PERCENT) AS S2_PERCENT FROM (SELECT TIME, PERCENT AS S1_PERCENT, NULL AS S2_PERCENT FROM HISTORY WHERE LINE = :1 UNION ALL SELECT TIME, NULL AS S1_PERCENT, PERCENT AS S2_PERCENT FROM HISTORY WHERE LINE = :2) X GROUP BY X.TIME";
		String actualStatement = DatabaseReader
				.createQueryStatementToLoadData(lines);

		assertEquals(expectedStatement, actualStatement);
	}
}
