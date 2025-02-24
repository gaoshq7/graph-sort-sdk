package cn.gsq.graph.exception;

/**
 * Project : galaxy
 * Class : cn.gsq.graph.exception.NonexistentVertexException
 *
 * @author : gsq
 * @date : 2023-03-28 11:30
 * @note : It's not technology, it's art !
 **/
public class NonexistentVertexException extends Exception {

    private static final long serialVersionUID = -5781554196572487458L;

    public NonexistentVertexException(final String string) {
        super(string);
    }

}
