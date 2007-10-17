package org.easygen.core.generators;

import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.VelocityContext;

public class DebuggableVelocityContext extends VelocityContext {

    private static final long serialVersionUID = 9033846851064645037L;

	private Map<String, Object> context;

    public DebuggableVelocityContext() {
        super(null, null);
        context = new HashMap<String, Object>();
    }

	public Map<String, Object> getContext() {
		return this.context;
	}

	@Override
	public Object internalGet(String key)
    {
        return context.get(key);
    }

	@Override
    public Object internalPut(String key, Object value)
    {
        return context.put(key, value);
    }

    @Override
    public boolean internalContainsKey(Object key)
    {
        return context.containsKey(key);
    }

    @Override
    public Object[] internalGetKeys()
    {
        return context.keySet().toArray();
    }

    @Override
    public Object internalRemove(Object key)
    {
        return context.remove(key);
    }

	@Override
    public Object clone()
    {
    	DebuggableVelocityContext clone = null;
//        try {
            clone = (DebuggableVelocityContext)super.clone();
            clone.context = new HashMap<String, Object>(context);
//        }
//        catch(CloneNotSupportedException ignored) {
//        	
//        }
        return clone;
    }
	
	@Override
	public String toString() {
		return context.toString();
	}
}
