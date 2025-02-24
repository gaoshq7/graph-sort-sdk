package cn.gsq.graph.exception;

/**
 * Project : galaxy
 * Class : cn.gsq.graph.exception.NonexistentEdgeException
 *
 * @author : gsq
 * @date : 2023-03-28 11:30
 * @note : It's not technology, it's art !
 **/
public class NonexistentEdgeException extends Exception {

    private static final long serialVersionUID = 2333789612971784166L;

	public NonexistentEdgeException(final String string) {
        super(string);
    }

}
