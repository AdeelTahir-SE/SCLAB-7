/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import org.junit.Test;

import java.time.Instant;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SocialNetworkTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */

    private static final Instant time1 = Instant.parse("2023-02-01T10:00:00Z");
    private static final Instant time2 = Instant.parse("2023-02-01T11:00:00Z");
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    // 1. Empty List of Tweets
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }

    // 2. Tweets Without Mentions
    @Test
    public void testTweetsWithoutMentions() {
        Tweet t1 = new Tweet(1, "alice", "Hello everyone", time1);
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(List.of(t1));
        assertTrue("expected empty map", graph.isEmpty());
    }

    // 3. Single Mention
    @Test
    public void testSingleMention() {
        Tweet t1 = new Tweet(1, "ernie", "Hi @bert", time1);
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(List.of(t1));

        assertTrue(graph.containsKey("ernie"));
        assertTrue(graph.get("ernie").contains("bert"));
    }

    // 4. Multiple Mentions
    @Test
    public void testMultipleMentions() {
        Tweet t1 = new Tweet(1, "ernie", "@bert @cookieMonster are cool", time1);
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(List.of(t1));

        Set<String> expected = new HashSet<>(Arrays.asList("bert", "cookiemonster"));
        assertEquals(expected, graph.get("ernie"));
    }

    // 5. Multiple Tweets from One User
    @Test
    public void testMultipleTweetsOneUser() {
        Tweet t1 = new Tweet(1, "ernie", "Hi @bert", time1);
        Tweet t2 = new Tweet(2, "ernie", "Also @cookie", time2);
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(List.of(t1, t2));

        Set<String> expected = new HashSet<>(Arrays.asList("bert", "cookie"));
        assertEquals(expected, graph.get("ernie"));
    }

    // 6. Empty Graph for influencers()
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue("expected empty list", influencers.isEmpty());
    }

    // 7. Single User Without Followers
    @Test
    public void testSingleUserWithoutFollowers() {
        Map<String, Set<String>> graph = Map.of("alice", Set.of());
        List<String> influencers = SocialNetwork.influencers(graph);
        assertEquals(List.of("alice"), influencers);
    }

    // 8. Single Influencer
    @Test
    public void testSingleInfluencer() {
        Map<String, Set<String>> graph = Map.of(
                "alice", Set.of("bob"),
                "charlie", Set.of("bob")
        );
        List<String> influencers = SocialNetwork.influencers(graph);
        assertEquals("bob should be most followed", "bob", influencers.get(0));
    }

    // 9. Multiple Influencers
    @Test
    public void testMultipleInfluencers() {
        Map<String, Set<String>> graph = Map.of(
                "alice", Set.of("bob", "charlie"),
                "dave", Set.of("charlie")
        );
        List<String> influencers = SocialNetwork.influencers(graph);
        assertEquals("charlie should have most followers", "charlie", influencers.get(0));
    }

    // 10. Tied Influence
    @Test
    public void testTiedInfluence() {
        Map<String, Set<String>> graph = Map.of(
                "alice", Set.of("bob"),
                "charlie", Set.of("dave")
        );
        List<String> influencers = SocialNetwork.influencers(graph);

        assertTrue(influencers.containsAll(List.of("bob", "dave")));
    }
    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */


}
