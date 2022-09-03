package cn.feng.ikun.friend;

import java.util.ArrayList;
import java.util.List;

public class FriendManager {
    public final List<String> friends = new ArrayList<>();
    public void addFriend(String friend) {
        friends.add(friend);
    }
    public void removeFriend(String friend) {
        friends.remove(friend);
    }
    public boolean isFriend(String name) {
        return friends.contains(name);
    }
}
