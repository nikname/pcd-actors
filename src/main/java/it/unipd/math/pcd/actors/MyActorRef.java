/**
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2015 Riccardo Cardin
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * <p/>
 *
 * @author Nicola Dalla Costa
 * @version 1.0
 * @since 1.0
 */

package it.unipd.math.pcd.actors;

/**
 * Personal implementation of ActorRef.
 *
 * @author Nicola Dalla Costa
 * @version 1.0
 * @since 1.0
 */
public class MyActorRef<T extends Message> implements ActorRef<T> {

    private MyActorSystem actorSystem;

    /**
     * One-argument constructor definition.
     *
     * @param actorSystem
     */
    public MyActorRef(MyActorSystem actorSystem) {
        this.actorSystem = actorSystem;
    }

    @Override
    public void send(T message, ActorRef to) {
        AbsActor<T> actor = (AbsActor<T>) actorSystem.getActorByRef(to);
        // storeMessage checks if the actor can receive new message
        actor.storeMessage(message, this);
    }

    @Override
    public int compareTo(ActorRef ref) {
        return (this == ref) ? 0 : -1;
    }
}
