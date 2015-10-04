package org.pnwg.tools.diff.context;

public class ContextUtil {

	/**
	 * Create a new context using provided {@link IContext#config()}
	 * 
	 * @param ctx
	 * @return
	 */
	public static IContext copy(IContext ctx) {
		ContextImpl newctx = new ContextImpl();
		newctx.setConfig(ctx.config());
		return newctx;
	}
}
