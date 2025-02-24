package cn.gsq.graph.exception;

/**
 * Project : galaxy
 * Class : cn.gsq.graph.exception.DagCycleException
 *
 * @author : gsq
 * @date : 2023-03-28 11:30
 * @note : It's not technology, it's art !
 **/
public final class DagCycleException extends RuntimeException {

    private static final long serialVersionUID = 2114201844906144728L;

    public DagCycleException(final String string) {
        super(string);
    }

}
