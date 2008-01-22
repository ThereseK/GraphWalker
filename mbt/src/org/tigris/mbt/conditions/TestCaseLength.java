package org.tigris.mbt.conditions;

public class TestCaseLength extends StopCondition {

	private int numberOfEdges;

	public boolean isFulfilled() {
		return machine.getNumberOfEdgesTravesed() >= numberOfEdges;
	}

	public TestCaseLength(int numberOfEdges) {
		this.numberOfEdges = numberOfEdges;
	}

	public double getFulfillment() {
		return machine.getNumberOfEdgesTravesed() / (double)numberOfEdges;
	}
}
