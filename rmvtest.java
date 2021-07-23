import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class rmvtest {
	
	private rmv a;
	
	@Before
	public void setUp() throws Exception {
		a=new rmv();
	}
	
	@Test
	public void test_1() {
		final ByteArrayOutputStream system_out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(system_out));
		String expected_result = "参数输入个数错误！\n请使用 'rmv 出库数量 配件ID 仓库ID' 的格式。\n";
		String[] args = {"2", "005"};
		a.check(args);
		assertEquals(expected_result, system_out.toString());
	}
	
	@Test
	public void test_2() {
		final ByteArrayOutputStream system_out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(system_out));
		String expected_result = "出库数量类型错误！\n";
		String[] args = {"a", "005", "002"};
		a.check(args);
		assertEquals(expected_result, system_out.toString());
	}
	
	@Test
	public void test_3() {
		final ByteArrayOutputStream system_out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(system_out));
		String expected_result = "出库数量应大于0！\n";
		String[] args = {"-2", "005", "002"};
		a.check(args);
		assertEquals(expected_result, system_out.toString());
	}
	
	@Test
	public void test_4() {
		final ByteArrayOutputStream system_out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(system_out));
		String expected_result = "配件号或仓库号不存在！\n";
		String[] args = {"2", "999", "002"};
		a.dosql(args);
		assertEquals(expected_result, system_out.toString());
	}
	
	@Test
	public void test_5() {
		final ByteArrayOutputStream system_out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(system_out));
		String expected_result = "配件号或仓库号不存在！\n";
		String[] args = {"2", "005", "zzz"};
		a.dosql(args);
		assertEquals(expected_result, system_out.toString());
	}
	
	@Test
	public void test_6() {
		final ByteArrayOutputStream system_out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(system_out));
		String expected_result = "库存不足！\n";
		String[] args = {"10000", "005", "002"};
		a.check(args);
		a.dosql(args);
		assertEquals(expected_result, system_out.toString());
	}
	
	@Test
	public void test_7() {
		final ByteArrayOutputStream system_out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(system_out));
		String expected_result = "出库成功！\n";
		String[] args = {"2", "005", "002"};
		a.check(args);
		a.dosql(args);
		assertEquals(expected_result, system_out.toString());
	}
}
