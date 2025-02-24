package cn.gsq.graph.exception;

/**
 * Project : galaxy
 * Class : cn.gsq.graph.exception.DuplicatedEdgeException
 *
 * @author : gsq
 * @date : 2023-03-28 11:30
 * @note : It's not technology, it's art !
 **/
public class DuplicatedEdgeException extends RuntimeException {

    private static final long serialVersionUID = -4908824144206579990L;

    public DuplicatedEdgeException(final String string) {
        super(string);
    }

}
