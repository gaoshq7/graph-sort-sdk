package cn.gsq.graph.task;

import java.util.UUID;

/**
 * Project : galaxy
 * Class : cn.gsq.graph.task.CompletionMessage
 *
 * @author : gsq
 * @date : 2023-03-28 13:11
 * @note : It's not technology, it's art !
 **/
public class CompletionMessage {

    UUID id;

    public CompletionMessage(UUID uuid) {
        id = uuid;
    }

    public UUID getFromUUID() {
        return id;
    }

}
