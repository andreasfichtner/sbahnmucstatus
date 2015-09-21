package sbahnmucstatustracker;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class DataExportTest {

	@Test
	public void testCreateQueryStatementToLoadData() {
		DataExport export = new DataExport();
		List<String> lines = new LinkedList<>();
		lines.add("S1");
		lines.add("S2");

		final String expectedStatement = "SELECT X.TIME, MAX(S1_PERCENT) AS S1_PERCENT, MAX(S2_PERCENT) AS S2_PERCENT FROM (SELECT TIME, PERCENT AS S1_PERCENT, 0 AS S2_PERCENT FROM HISTORY WHERE LINE = 'S1' UNION ALL SELECT TIME, 0 AS S1_PERCENT, PERCENT AS S2_PERCENT FROM HISTORY WHERE LINE = 'S2') X GROUP BY X.TIME";
		String actualStatement = export.createQueryStatementToLoadData(lines);

		assertEquals(expectedStatement, actualStatement);
	}
}
