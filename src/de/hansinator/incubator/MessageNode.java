package de.hansinator.incubator;

import de.hansinator.message.MessageObject;

public interface MessageNode<T extends MessageObject> {

	public boolean onMessageReceived(T msg);
}