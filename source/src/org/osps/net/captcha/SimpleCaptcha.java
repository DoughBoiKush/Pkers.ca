package org.osps.net.captcha;

import org.osps.util.Misc;

public class SimpleCaptcha implements Captcha {

	enum Operation {
		ADD,
		SUB,
		MULT;
	}
	
	
	byte[] values;
	Operation op;
	
	@Override
	public Object getKey(int hash) {
		try {
			values = new byte[3];
			for(int i = 0; i < 2; i++)
				values[i] = (byte) Misc.random(12);
			if(values[1] > values[0])
				values[0] += values[1];
			op = Operation.values()[(int) (Math.random() * Operation.values().length)];
			values[2] = (byte) op.ordinal();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return values;
	}

	@Override
	public Object getAnswer() {
		int ans = 0;
		switch(op) {
		case ADD:
			ans = values[0] + values[1];
			break;
		case SUB:
			ans = values[0] - values[1];
			break;
		case MULT:
			ans = values[0] * values[1];
			break;
		}
		return ans;
	
	}

	@Override
	public int getExpectedLength() {
		return 1;
	}
	
}
