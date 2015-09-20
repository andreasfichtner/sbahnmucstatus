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

		final String expectedStatement = "SELECT X.TIME, MAX(S1.PERCENT), MAX(S2.PERCENT) FROM (SELECT S1.PERCENT AS S1, 0 AS S2 FROM HISTORY WHERE LINE = 'S1' UNION ALL SELECT 0 AS S1, S2.PERCENT AS S2 FROM HISTORY WHERE LINE = 'S2') X GROUP BY X.TIME";
		String actualStatement = export.createQueryStatementToLoadData(lines);

		System.err.println(actualStatement);
		assertEquals(expectedStatement, actualStatement);
	}
}
