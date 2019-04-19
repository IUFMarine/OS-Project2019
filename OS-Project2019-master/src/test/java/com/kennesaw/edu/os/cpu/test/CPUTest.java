package com.kennesaw.edu.os.cpu.test;

import org.junit.Assert;
import org.junit.Test;

import com.kennesaw.edu.os.cpu.CPU;

public class CPUTest {

	@Test
	public void canCreateEmptyCPU() {
		Assert.assertNotNull(new CPU());
	}
}
