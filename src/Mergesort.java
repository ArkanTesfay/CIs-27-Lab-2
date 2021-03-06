
import java.util.Arrays;

public class Mergesort {

    // You can use the log() method to do debugging-by-printf,
    // and set debug to true or false to turn debug output off or on
    //private static boolean debug = false;
    private static boolean debug = true;

    private static boolean less(Comparable v1, Comparable v2) {
        return v1.compareTo(v2) < 0;
    }


    // Merges the right sorted linked list into the left sorted
    // linked list. (Returns the list containing all of the sorted
    // elements; this will either be <left> or <right>, as no new
    // elements are allocated.)
    private static ListNode merge(ListNode left, ListNode right) {
        // Make sure our preconditions hold
        assert (left == null || left.isSorted());
        assert (right == null || right.isSorted());

        // If either left or right is empty, return the other
        if (left == null) {
            return right;
        } else if (right == null) {
            return left;
        }
        else {
            // Both are non-empty; find which head element is smaller
            int leftLen = left.length();
            int rightLen = right.length();
            log("merging " + left.toString() + " and " + right.toString());
            Comparable leftVal = left.value();
            Comparable rightVal = right.value();
            ListNode head, newLeft, newRight;
            if (less(rightVal, leftVal)) {
                // The result list will be:
                // right.head, followed by the result of merging right.tail with left
                head = right;
                newLeft = right.next();
                newRight = left;
            } else {
                // The result list will be:
                // left.head, followed by the result of merging left.tail with right
                head = left;
                newLeft = left.next();
                newRight = right;
            }

            // Create the tail by recursively merging the two lists, minus the
            // smallest element
            ListNode tail = merge(newLeft, newRight);

            // Connect the tail to the head
            head.setNext(tail);
            tail.setPrev(head);

            assert (head.length() == leftLen + rightLen);
            return head;
        }
    }

    // Merges the right sorted linked list into the left sorted linked list
    // At the end, <left> will contain all the elements from both <left>
    // and <right>
    private static void merge(LinkedList left, LinkedList right) {
        log("merging " + left.toString() + " with " + right.toString());
        assert left.isSorted();
        assert right.isSorted();

        int leftLen = left.length();
        int rightLen = right.length();

        if (leftLen == 0 || rightLen == 0) {
            // Nothing to do
            return;
        }
        // Both heads are non-null
        ListNode newFirst = merge(left.head(), right.head());
        left.setHead(newFirst);

        assert left.isSorted();
    }

    // Given a list node, return the last element in the maximal increasing
    // subsequence starting at l (which may be null).
    // See the Lab 2 assignment for the definition of a maximal increasing subsequence.
    private static ListNode findMaximalIncreasingSubsequence(ListNode l) {
        //if the head is null return null
        if(l == null) {
            return null;
        }
        //if the second node is null return null
        else if(l.next() == null) {
            return l;
        }
        else if(less(l.next().value(), l.value())) {//if the next node is less then current node stop sub-list
            //System.out.println(l.value());
            return l;
        }
        // or if the next one is larger continue sub-list
            return findMaximalIncreasingSubsequence(l.next());

    }

    // Sort the list using the natural mergesort algorithm.
    public static void naturalMergesort(LinkedList l) {
        int N = l.length();

        ListNode head = l.head();

        //ListNode newHead = head;//maybe don't need

        if (N == 0) {
            return; // Nothing to do
        }


        // The code should work by maintaining two sorted sequences:
        // seq1 and seq2. At all times, each sublist (seq1 and seq2) is sorted.
        // We initialize them by finding the first two sorted subsequences.


        // Inside the while loop, your code should change seq1 and seq2
        // (how you do that is up to you.) You know you're done when
        // seq1 represents the entire list.

        // Hint: before you pass two sublists to merge(), you need to
        // "disconnect" them from the rest of the list and keep pointers
        // to the rest of the list. See bottomUpMergesort() for an example
        // of how to do that.

        // First we have to initialize seq1 and seq2.
        ListNode headOfSeq1 = head; //was normal head before...
        ListNode lastInSeq1 = findMaximalIncreasingSubsequence(headOfSeq1);
        ListNode headOfSeq2 = lastInSeq1;
        if (headOfSeq2.length() == 1) {
            System.out.println("Yo");
            return; // This means the list is already sorted
        } else {
            System.out.println("Out");
            headOfSeq2 = headOfSeq2.next();
        }
        ListNode lastInSeq2 = findMaximalIncreasingSubsequence(headOfSeq2);

        // Now, headOfSeq1 points to the first element in seq1 and lastInSeq1 points to the last element in seq1.
        // Likewise, headOfSeq2 points to the first element in seq2 and lastInSeq2 points to the last element in seq2.
        assert(headOfSeq1 != null);
        assert(lastInSeq1 != null);
        assert(headOfSeq2 != null);
        assert(lastInSeq2 != null);

        // restOfList will point to the beginning of the part of the list that hasn't been explored yet.
        ListNode restOfList = lastInSeq2.next();

        // As long as lastInSeq2 isn't the last node in the list...
        while (lastInSeq2 != null) {//was .next()

            // seq1 should have a non-null beginning and end
            assert(headOfSeq1.prev() == null);
            assert(lastInSeq1.next() == headOfSeq2);
            // And seq2 should immediately follow seq1
            assert(headOfSeq2.prev() == lastInSeq1);

            log("first sorted sequence: " + headOfSeq1.toString());
            log("second sorted sequence: " + headOfSeq2.toString());


			/*
			  Fill in this code! You can use merge() and findMaximalIncreasingSubsequence().
			 */

            ListNode saved;
            if(lastInSeq2.next() != null) {
                saved = lastInSeq2.next();//there are nodes after so save them
            }
            else {
                saved = null;//means at end of list
            }

            // Disconnect the left half from the prefix
            headOfSeq1.setPrev(null);
            // Disconnect the left half from the right half
            lastInSeq1.setNext(null);
            // Disconnect the right half from the left half
            headOfSeq2.setPrev(null);
            // Disconnect the right half from the suffix
            lastInSeq2.setNext(null);

            ListNode combined = merge(headOfSeq1, headOfSeq2);
            //Now time to recombine...

            // Reconnect the merged left and right halves to the prefix
            //Probably did wrong, refix if it is...
            headOfSeq1 = combined;
            headOfSeq1.setPrev(null);

            // Reconnect the merged left and right halves to the suffix
            combined.last().setNext(saved);
            if(saved != null) {
                saved.setPrev(combined.last());
            }

            lastInSeq1 = findMaximalIncreasingSubsequence(headOfSeq1);
            headOfSeq2 = lastInSeq1.next();

            System.out.println("************ " + headOfSeq2 + " and " + lastInSeq1);
            if(headOfSeq2 != null) {
                headOfSeq2.setPrev(lastInSeq1);//was commeneted out...
            }

            lastInSeq2 = findMaximalIncreasingSubsequence(headOfSeq2);

            if(lastInSeq2 != null) {//using for debugging...
                System.out.println(lastInSeq2.value());
            }
            else {
                System.out.println(lastInSeq1.value());
            }
        }


        // Update the head, since the order may have changed
        l.setHead(headOfSeq1);
        System.out.println("Final: " + l);


        assert(l.isSorted());
    }



    public static void bottomUpMergesort(LinkedList l) {
        int N = l.length();

        ListNode head = l.head();
        ListNode newHead = head;

        for(int sz = 1; sz < N; sz = sz+sz) { // sz is the length of one sublist to pass into merge()

            // We maintain six pointers:
            // lastBeforeLeftHalf://----
            //  * Points to last element before head of left half (may be null if left half begins with the head of the list)
            // firstInLeftHalf:
            //  * Pointer to head of left half (must be non-null)
            // lastInLeftHalf:
            //  * Pointer to last element in left half (must be non-null)
            // firstInRightHalf:
            //  * Pointer to head of right half (must be non-null)
            // lastInRightHalf:
            //  * Pointer to last element in right half (must be non-null)
            // firstAfterRightHalf:
            //  * Pointer to next element after end of right half (may be null if right half ends with the end of the list


            // Think of the entire list as:
            // prefix + left + right + suffix
            // where left and right are the two sublists to pass into merge(), and prefix and suffix are not
            // being worked on during the current iteration of the loop

            // We start off by making firstInLeftHalf point to the head of the list.
            ListNode firstInLeftHalf = newHead;

            for(int lo = 0; lo < N-sz; lo += sz + sz) { // lo: starting position for sublist
                System.out.println("lo is " + lo);
                assert (firstInLeftHalf != null);

                // The length of the left half is (sz - 1)
                int mid = lo+sz-1;

                // The right half ends at (mid + sz) or the end of the list, whichever comes first if we start from mid
                int hi = Math.min(mid + sz, N - 1);

                System.out.println("sz = " + sz + " lo = " + lo + " mid = " + mid + " hi = " + hi + " N = " + N);
                log("firstInLeftHalf = " + firstInLeftHalf.value().toString());

                // The end of <prefix>
                ListNode lastBeforeLeftHalf = firstInLeftHalf.prev();

                // Find the end of the left half by counting up from 0 to (sz - 1)
                ListNode lastInLeftHalf = firstInLeftHalf;
//				System.out.println("B4 Loop: lastLH " + lastInLeftHalf.value());

                for(int i = 0; i < sz - 1 && lastInLeftHalf != null; i++) {
                    lastInLeftHalf = lastInLeftHalf.next();
//					System.out.println("In-LOOP: " + lastInLeftHalf.value());
                }
//				System.out.println("After Loop: lastLH " + lastInLeftHalf.value());

                assert (lastInLeftHalf != null);

                // That gives us the start of the right half
                ListNode firstInRightHalf = lastInLeftHalf.next();
                assert (firstInRightHalf != null);


                // Find the end of the right half by counting up to <hi - mid - 1>, which
                // is the length of the right half (it might be shorter than the left half)
                ListNode lastInRightHalf = firstInRightHalf;
                for(int i = 0; i < hi - mid - 1 && lastInRightHalf != null; i++) {
                    lastInRightHalf = lastInRightHalf.next();
                }
                assert (lastInRightHalf != null);

                // That gives us the start of the suffix
                ListNode firstAfterRightHalf = lastInRightHalf.next();

                // Disconnect the left half from the prefix
                firstInLeftHalf.setPrev(null);
                // Disconnect the left half from the right half
                lastInLeftHalf.setNext(null);
                // Disconnect the right half from the left half
                firstInRightHalf.setPrev(null);
                // Disconnect the right half from the suffix
                lastInRightHalf.setNext(null);

                if (lastBeforeLeftHalf != null) {
                    log("lastBeforeLeftHalf = " + lastBeforeLeftHalf.value().toString());
                }
                log("lastInLeftHalf = " + lastInLeftHalf.value().toString());

                // Now we can merge the left and right halves.
                // We give it a name since sorting might change the value of the first
                // element.
                ListNode newMiddle = merge(firstInLeftHalf, firstInRightHalf);
                log("newMiddle = " + newMiddle.toString());

                // Reconnect the merged left and right halves to the prefix
                if (lastBeforeLeftHalf != null) {//so connects whats before this left half that was already sorted to the newly merged part...
//					System.out.println(lastBeforeLeftHalf.value() + " and " + newMiddle);
                    lastBeforeLeftHalf.setNext(newMiddle);
//					System.out.println(lastBeforeLeftHalf.next());
//					System.out.println();
                } else {
                    // If the prefix was empty, update the head (to be returned at the end)
                    newHead = newMiddle;
                }
                newMiddle.setPrev(lastBeforeLeftHalf);

                // Reconnect the merged left and right halves to the suffix
                if (firstAfterRightHalf != null) {
                    firstAfterRightHalf.setPrev(newMiddle.last());
                }
                newMiddle.last().setNext(firstAfterRightHalf);


                // Update the pointers for the next iteration of the loop:
                // we'll start from what was the suffix in this iteration
                firstInLeftHalf = firstAfterRightHalf;
            }

            log("with sz = " + sz + " : " + newHead.toString());
        }

        // Update the head, since the order may have changed
        l.setHead(newHead);
        assert(l.isSorted());
    }

    /******* Testing helper methods *********/
    private static LinkedList stringToList(String s) {
        Character[] testArray = new Character[s.length()];
        for(int i = 0; i < testArray.length; i++) {
            testArray[i] = s.charAt(i);
        }
        return LinkedList.arrayToList(testArray);
    }

    private static void log(String s) {
        if (debug) {
            System.out.println(s);
            System.out.flush();
        }
    }

    private static String sortString(String s) {
        Character[] charArray = new Character[s.length()];
        for(int i = 0; i < charArray.length; i++) {
            charArray[i] = s.charAt(i);
        }
        Arrays.sort(charArray);
        String result = "";
        for(int i = 0; i < charArray.length; i++) {
            result += charArray[i];
        }
        return result;
    }

    private static void sortTest(String s) {
        log("unsorted string = " + s);
        LinkedList l = stringToList(s);
        String sortedString = sortString(s);
        // If you want to experiment with bottomUpMergesort, you can
        // uncomment out the next line and comment out the call to naturalMergesort
        //bottomUpMergesort(l);
        naturalMergesort(l);
        log("sorted string = " + sortedString);
        assert(l.equals(stringToList(sortedString)));
        assert(l.isSorted());
    }
    /***************/

    /****** Tests *******/
    private static void testMerge() {
        LinkedList none1 = new LinkedList();
        LinkedList none2 = new LinkedList();
        LinkedList one1 = new LinkedList();
        one1.insert(2);
        LinkedList one2 = new LinkedList();
        one2.insert(1);

        LinkedList lots = stringToList("MERGESORTEXAMPLE");


        log("checking empty list...");
        // empty lists
        merge(none1, none2);
        assert(none1.length() == 0);
        // length 1
        merge(one1, one2);
        assert(one1.length() == 2);
        assert(one1.isSorted());

        LinkedList sortedList1 = stringToList("EEGMORRS");
        LinkedList sortedList2 = stringToList("AEELMPTX");

        log(sortedList1.toString());
        log(sortedList2.toString());
        log("checking nonempty lists...");
        merge(sortedList1, sortedList2);
        log("****result = " + sortedList1.toString());
        log("reference = " + stringToList("AEEEEGLMMOPRRSTX").toString());
        assert(sortedList1.equals(stringToList("AEEEEGLMMOPRRSTX")));
    }

    public static void main(String[] args) {
        //assert false: "Testing assertions";

        //All exmaples working ATM

        //testMerge();

        //	sortTest("5678");
        sortTest("MERGESORTEXAMPLE");
        //	sortTest("");
        //	sortTest("A");
        //	sortTest("bottomupmergesortconsistsofasequenceofpassesoverthewholearray");
        //	sortTest("thefirststepinastudyofcomplexityistoestablishamodelofcomputation.generally,researchersstrivetounderstandthesimplestmodelrelevanttoaproblem.");
    }
}