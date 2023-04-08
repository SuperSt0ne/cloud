package com.stone.common.http.context;

public final class RequestContext {

    private static final ThreadLocal<RequestContext> context = ThreadLocal.withInitial(RequestContext::new);

    private boolean needEncryptResult;

    public static boolean needEncryptResult() {
        return context.get().needEncryptResult;
    }

    public static void setNeedEncryptResult(boolean needEncryptResult) {
        context.get().needEncryptResult = needEncryptResult;
    }

}
