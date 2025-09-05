package swen90006.irms;

import org.junit.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

public class PartitioningTests {
    // The IRMS instance variable irms is shared across all test methods in this class
    protected IRMS irms;

     /**
     * The setup method annotated with "@Before" runs before each test.
     * By default, it initializes the IRMS instance and creates a dummy user.
     * Use this method to set up any common test data or state.
     */

    @Before
    public void setUp() throws DuplicateAnalystException, InvalidAnalystNameException, InvalidPasswordException {
        irms = new IRMS();
//        irms.registerAnalyst("analystA", "Password1!");
        irms.registerAnalyst("Admin", "password1!");
    }

     /**
     * The teardown method annotated with "@After" runs after each test.
     * It's useful for cleaning up resources or resetting states.
     * Currently, this method doesn't perform any actions, but you can customize it as needed.
     */
    @After
    public void tearDown() {
        // No resources to clean up in this example, but this is where you would do so if needed
        irms = null;
    }

    /**
     * This is a basic example test annotated with "@Test" to demonstrate how to use assertions in JUnit.
     * The assertEquals method checks if the expected value matches the actual value.
     */

    @Test 
    public void aTest(){
        final int expected = 2;
        final int actual = 1 + 1;
        // Use of assertEquals to verify that the expected value matches the actual value
        assertEquals(expected, actual);
    }

    /**
     * This test checks if the InvalidAnalystNameException is correctly thrown when registering with an invalid analyst name.
     * The expected exception is specified in the @Test annotation.
     */
    @Test(expected = InvalidAnalystNameException.class)
    public void anExceptionTest()
            throws DuplicateAnalystException, InvalidAnalystNameException, InvalidPasswordException {
        // Test registration with an invalid username
        // to test whether the appropriate exception is thrown.
        irms.registerAnalyst("aa", "Password1!");
    }

     /**
     * This is an example of a test that is designed to fail.
     * It shows how to include an error message to provide feedback when a test doesn't pass.
     */
//    @Test
//    public void aFailedTest() {
//        // This test currently fails to demonstrate how JUnit reports errors
//        final int expected = 2;
//        final int actual = 1 + 2;
//        // Uncomment the following line to observe a test failure.
//        assertEquals("Some failure message", expected, actual);
//    }

    // ADD YOUR TESTS HERE
    // This is the section where you will add your own tests.
    // Follow the examples above to create your tests.

     // worked answer: (expected = someException) is only used when
     //                 expecting a certain exception

     // The following are tests for registerAnalyst
     @Test(expected = NoSuchAnalystException.class)
     public void validRegisterAnalystTest()
             throws NoSuchAnalystException, UnauthenticatedAnalystException, DuplicateAnalystException, InvalidAnalystNameException, InvalidPasswordException {
         // Selected from EC_valid_pass
         String validName = "Jane";
         String validPassword = "password1$";
         // Analyst is not expected to exist prior to registration so throws expected exception
         irms.isRegistered(validName);
         irms.registerAnalyst(validName, validPassword);
         // Analyst is expected to exist after registration
         boolean expectedRegistered = true;
         boolean actualRegistered = irms.isRegistered(validName);
         assertEquals(expectedRegistered, actualRegistered);
         // New analyst account has a default role of MEMBER
         IRMS.Role expectedRole = IRMS.Role.MEMBER;
         IRMS.Role actualRole = irms.getAnalystRole(validName);
         assertEquals(expectedRole, actualRole);
         // New analyst account has an authentication status of NOT_AUTHENTICATED
         boolean expectedAuthentication = false;
         boolean actualAuthentication = irms.isAuthenticated(validName);
         assertEquals(expectedAuthentication, actualAuthentication);
     }
     @Test(expected = DuplicateAnalystException.class)
     public void duplicateAnalystTest()
             throws DuplicateAnalystException, InvalidAnalystNameException, InvalidPasswordException {
         // Selected from EC_invalid_duplicate
         irms.registerAnalyst("Admin", "password1!"); // registered already in setUp
     }
    @Test(expected = InvalidAnalystNameException.class)
    public void analystNameInvalidLengthTest()
            throws DuplicateAnalystException, InvalidAnalystNameException, InvalidPasswordException {
        // Selected from EC_invalid_length
         irms.registerAnalyst("Adm", "password1!");
    }
    @Test(expected = InvalidAnalystNameException.class)
    public void analystNameInvalidCharacterTest()
            throws DuplicateAnalystException, InvalidAnalystNameException, InvalidPasswordException {
        // Selected from EC_invalid_char
         irms.registerAnalyst("Adm$", "password1$");
    }
    @Test(expected = InvalidPasswordException.class)
    public void passwordInvalidLengthBelow10Test()
            throws DuplicateAnalystException, InvalidAnalystNameException, InvalidPasswordException {
        // Selected from EC_invalid_length_10
         irms.registerAnalyst("Crane", "passit1$");
    }
    @Test(expected = InvalidPasswordException.class)
    public void passwordInvalidLengthAbove16Test()
            throws DuplicateAnalystException, InvalidAnalystNameException, InvalidPasswordException {
        // Selected from EC_invalid_length_16
         irms.registerAnalyst("Crane", "password1$1234567");
    }
    @Test(expected = InvalidPasswordException.class)
    public void passwordInvalidLetterTest()
            throws DuplicateAnalystException, InvalidAnalystNameException, InvalidPasswordException {
        // Selected from EC_invalid_password_letter
         irms.registerAnalyst("Crane", "1234567891$");
    }
    @Test(expected = InvalidPasswordException.class)
    public void passwordInvalidDigitTest()
            throws DuplicateAnalystException, InvalidAnalystNameException, InvalidPasswordException {
        // Selected from EC_invalid_password_digit
         irms.registerAnalyst("Crane", "$passwords$");
    }
    @Test(expected = InvalidPasswordException.class)
    public void passwordInvalidSpecialTest()
            throws DuplicateAnalystException, InvalidAnalystNameException, InvalidPasswordException {
        // Selected from EC_invalid_password_special
         irms.registerAnalyst("Crane", "1passwords12");
    }
    // The following are tests for authenticate
    @Test
    public void authenticateTest()
            throws IncorrectPasswordException, NoSuchAnalystException, DuplicateAnalystException, InvalidAnalystNameException, InvalidPasswordException {
        // Selected from EC_valid
        irms.authenticate("Admin", "password1!");
        boolean expectedAuthentication = true;
        boolean actualAuthentication = irms.isAuthenticated("Admin");
        assertEquals(expectedAuthentication, actualAuthentication);
    }
    @Test(expected = NoSuchAnalystException.class)
    public void authenticateAnalystInvalidTest()
            throws IncorrectPasswordException, NoSuchAnalystException, DuplicateAnalystException, InvalidAnalystNameException, InvalidPasswordException {
        // Selected from EC_invalid_password
        irms.authenticate("Jan", "password1!");
        boolean expectedAuthentication = false;
        boolean actualAuthentication = irms.isAuthenticated("Jan");
        assertEquals(expectedAuthentication, actualAuthentication);
    }
    @Test(expected = IncorrectPasswordException.class)
    public void authenticatePasswordInvalidTest()
            throws IncorrectPasswordException, NoSuchAnalystException, DuplicateAnalystException, InvalidAnalystNameException, InvalidPasswordException {
        // Selected from EC_invalid_password
        irms.authenticate("Admin", "password1!2");
        boolean expectedAuthentication = false;
        boolean actualAuthentication = irms.isAuthenticated("Admin");
        assertEquals(expectedAuthentication, actualAuthentication);
    }
    // requestSupervisorAccess
    @Test
    public void requestSupervisorAccess1234BadgeTest()
            throws InvalidBadgeIDException, NoSuchAnalystException {
        // Selected from EC_V_badgeID_1234
        irms.requestSupervisorAccess("Admin", "1234");
    }
    @Test
    public void requestSupervisorAccess1235BadgeTest()
            throws InvalidBadgeIDException, NoSuchAnalystException {
        // Selected from EC_V_badgeID_1235
        irms.requestSupervisorAccess("Admin", "1235");
    }
    @Test(expected = InvalidBadgeIDException.class)
    public void requestSupervisorAccessInvalidBadgeTest()
            throws InvalidBadgeIDException, NoSuchAnalystException {
        // Selected from EC_IV_badgeID
        irms.requestSupervisorAccess("Admin", "Dud%");
    }
    @Test(expected = NoSuchAnalystException.class)
    public void requestSupervisorAccessInvalidAnalystTest()
            throws InvalidBadgeIDException, NoSuchAnalystException {
        // Selected from EC_IV_analyst
        irms.requestSupervisorAccess("Jan", "1234");
    }
    // submitIncident
    @Test
    public void submitIncidentAsSupervisorTest()
            throws UnauthenticatedAnalystException, IncidentRejectException, InvalidRatingException, DuplicateIncidentException, UnauthenticatedAnalystException, InvalidBadgeIDException, InvalidPasswordException, IncorrectPasswordException, NoSuchAnalystException, InvalidAnalystNameException, DuplicateAnalystException {
        // Selected from EC_V_supervisor
        String supervisor = "Superuser";
        irms.registerAnalyst(supervisor, "Strong%word1");
        irms.authenticate(supervisor, "Strong%word1");
        irms.requestSupervisorAccess(supervisor, "1234");
        IRMS.Role expectedRole = IRMS.Role.SUPERVISOR;
        IRMS.Role actualRole = irms.getAnalystRole(supervisor);
        assertEquals(expectedRole, actualRole);
        irms.submitIncident(supervisor, "0", 1);
        boolean expectedSubmitIncidentCheck = true;
        boolean actualSubmitIncidentCheck = irms.isSavedIncident("0", 1);
        assertEquals(expectedSubmitIncidentCheck, actualSubmitIncidentCheck);
    }
    @Test
    public void submitIncidentAsMemberTest()
            throws UnauthenticatedAnalystException, IncidentRejectException, InvalidRatingException, DuplicateIncidentException, UnauthenticatedAnalystException, InvalidBadgeIDException, InvalidPasswordException, IncorrectPasswordException, NoSuchAnalystException, InvalidAnalystNameException, DuplicateAnalystException {
        // Selected from EC_V_member0
        irms.authenticate("Admin", "password1!");
        IRMS.Role expectedRole = IRMS.Role.MEMBER;
        IRMS.Role actualRole = irms.getAnalystRole("Admin");
        assertEquals(expectedRole, actualRole);
        irms.submitIncident("Admin", "0", 1);
        boolean expectedSubmitIncidentCheck = true;
        boolean actualSubmitIncidentCheck = irms.isSavedIncident("0", 1);
        assertEquals(expectedSubmitIncidentCheck, actualSubmitIncidentCheck);
    }
    @Test
    public void submitRepeatIncidentAsMemberTest()
            throws UnauthenticatedAnalystException, IncidentRejectException, InvalidRatingException, DuplicateIncidentException, UnauthenticatedAnalystException, InvalidBadgeIDException, InvalidPasswordException, IncorrectPasswordException, NoSuchAnalystException, InvalidAnalystNameException, DuplicateAnalystException {
        // Selected from EC_V_member1
        irms.authenticate("Admin", "password1!");
        IRMS.Role expectedRole = IRMS.Role.MEMBER;
        IRMS.Role actualRole = irms.getAnalystRole("Admin");
        assertEquals(expectedRole, actualRole);
        irms.submitIncident("Admin", "0", 1);
        boolean expectedSubmitIncidentCheck = true;
        boolean actualSubmitIncidentCheck = irms.isSavedIncident("0", 1);
        assertEquals(expectedSubmitIncidentCheck, actualSubmitIncidentCheck);
        irms.submitIncident("Admin", "1", 3); // since the incident list is not empty, this rating must be higher than the lowest
        boolean expectedSubmitIncidentRepeatCheck = true;
        boolean actualSubmitIncidentRepeatCheck = irms.isSavedIncident("1", 3);
        assertEquals(expectedSubmitIncidentRepeatCheck, actualSubmitIncidentRepeatCheck);
        irms.submitIncident("Admin", "2", 2); // since the incident list is not empty, this rating must be higher than the lowest
        boolean expectedSubmitIncidentRepeatRepeatCheck = true;
        boolean actualSubmitIncidentRepeatRepeatCheck = irms.isSavedIncident("2", 2);
        assertEquals(expectedSubmitIncidentRepeatRepeatCheck, actualSubmitIncidentRepeatRepeatCheck);
    }
    @Test(expected = NoSuchAnalystException.class)
    public void submitIncidentAsUnregisteredTest()
            throws UnauthenticatedAnalystException, IncidentRejectException, InvalidRatingException, DuplicateIncidentException, UnauthenticatedAnalystException, InvalidBadgeIDException, InvalidPasswordException, IncorrectPasswordException, NoSuchAnalystException, InvalidAnalystNameException, DuplicateAnalystException {
        // Selected from EC_IV_register
        irms.submitIncident("Jant", "1", 0);
    }
    @Test(expected = UnauthenticatedAnalystException.class)
    public void submitIncidentAsUnauthenticatedTest()
            throws UnauthenticatedAnalystException, IncidentRejectException, InvalidRatingException, DuplicateIncidentException, UnauthenticatedAnalystException, InvalidBadgeIDException, InvalidPasswordException, IncorrectPasswordException, NoSuchAnalystException, InvalidAnalystNameException, DuplicateAnalystException {
        // Selected from EC_IV_authenticated
        irms.submitIncident("Admin", "0", 9);
    }
    @Test(expected = DuplicateIncidentException.class)
    public void submitDuplicateIncidentTest()
            throws UnauthenticatedAnalystException, IncidentRejectException, InvalidRatingException, DuplicateIncidentException, UnauthenticatedAnalystException, InvalidBadgeIDException, InvalidPasswordException, IncorrectPasswordException, NoSuchAnalystException, InvalidAnalystNameException, DuplicateAnalystException {
        // Selected from EC_IV_incidentID
        irms.authenticate("Admin", "password1!");
        IRMS.Role expectedRole = IRMS.Role.MEMBER;
        IRMS.Role actualRole = irms.getAnalystRole("Admin");
        assertEquals(expectedRole, actualRole);
        irms.submitIncident("Admin", "0", 1);
        irms.submitIncident("Admin", "0", 9);
    }
    @Test(expected = InvalidRatingException.class)
    public void submitInvalidNegativeRatingTest()
            throws UnauthenticatedAnalystException, IncidentRejectException, InvalidRatingException, DuplicateIncidentException, UnauthenticatedAnalystException, InvalidBadgeIDException, InvalidPasswordException, IncorrectPasswordException, NoSuchAnalystException, InvalidAnalystNameException, DuplicateAnalystException {
        // Selected from EC_IV_negative_rating
        irms.authenticate("Admin", "password1!");
        IRMS.Role expectedRole = IRMS.Role.MEMBER;
        IRMS.Role actualRole = irms.getAnalystRole("Admin");
        assertEquals(expectedRole, actualRole);
        irms.submitIncident("Admin", "0", -1);
    }
    @Test(expected = InvalidRatingException.class)
    public void submitInvalidRatingAbove9Test()
            throws UnauthenticatedAnalystException, IncidentRejectException, InvalidRatingException, DuplicateIncidentException, UnauthenticatedAnalystException, InvalidBadgeIDException, InvalidPasswordException, IncorrectPasswordException, NoSuchAnalystException, InvalidAnalystNameException, DuplicateAnalystException {
        // Selected from EC_IV_rating
        irms.authenticate("Admin", "password1!");
        IRMS.Role expectedRole = IRMS.Role.MEMBER;
        IRMS.Role actualRole = irms.getAnalystRole("Admin");
        assertEquals(expectedRole, actualRole);
        irms.submitIncident("Admin", "0", 10);
    }
    @Test(expected = IncidentRejectException.class)
    public void submitInvalidRatingByMemberTest()
            throws UnauthenticatedAnalystException, IncidentRejectException, InvalidRatingException, DuplicateIncidentException, UnauthenticatedAnalystException, InvalidBadgeIDException, InvalidPasswordException, IncorrectPasswordException, NoSuchAnalystException, InvalidAnalystNameException, DuplicateAnalystException {
        // Selected from EC_IV_rating_by_member
        irms.authenticate("Admin", "password1!");
        IRMS.Role expectedRole = IRMS.Role.MEMBER;
        IRMS.Role actualRole = irms.getAnalystRole("Admin");
        assertEquals(expectedRole, actualRole);
        irms.submitIncident("Admin", "0", 1);
        irms.submitIncident("Admin", "1", 1);
    }
    // getIncident
    @Test
    public void getIncidentTest()
            throws UnauthenticatedAnalystException, IncidentRejectException, InvalidRatingException, DuplicateIncidentException, UnauthenticatedAnalystException, InvalidBadgeIDException, InvalidPasswordException, IncorrectPasswordException, NoSuchAnalystException, InvalidAnalystNameException, DuplicateAnalystException, IndexOutOfBoundsException {
        // Selected from EC_V_index
        irms.authenticate("Admin", "password1!");
        IRMS.Role expectedRole = IRMS.Role.MEMBER;
        IRMS.Role actualRole = irms.getAnalystRole("Admin");
        assertEquals(expectedRole, actualRole);
        irms.submitIncident("Admin", "0", 1);
        boolean expectedSubmitIncidentCheck = true;
        boolean actualSubmitIncidentCheck = irms.isSavedIncident("0", 1);
        assertEquals(expectedSubmitIncidentCheck, actualSubmitIncidentCheck);
//        SimpleEntry<String, Integer> actualIncidentListItem = irms.getIncident("Admin",0);
//        SimpleEntry<String, Integer> expectedIncidentListItem = new SimpleEntry<String, Integer>("Admin",0);
//        assertEquals(expectedIncidentListItem, actualIncidentListItem);
    }
    @Test(expected = NoSuchAnalystException.class)
    public void getIncidentNoSuchAnalystTest()
            throws NoSuchAnalystException, IndexOutOfBoundsException {
         // Selected from EC_IV_registered
         irms.getIncident("Stranger",0);
    }
    @Test(expected = IndexOutOfBoundsException.class)
    public void getIncidentNegativeIndexTest()
            throws NoSuchAnalystException, IndexOutOfBoundsException {
        // Selected from EC_IV_index_below
        irms.getIncident("Admin",-1);
    }
    @Test(expected = IndexOutOfBoundsException.class)
    public void getIncidentIndexAboveRangeTest()
            throws UnauthenticatedAnalystException, IncidentRejectException, InvalidRatingException, DuplicateIncidentException, UnauthenticatedAnalystException, InvalidBadgeIDException, InvalidPasswordException, IncorrectPasswordException, NoSuchAnalystException, InvalidAnalystNameException, DuplicateAnalystException, IndexOutOfBoundsException {
        // Selected from EC_IV_index_above
        irms.authenticate("Admin", "password1!");
        IRMS.Role expectedRole = IRMS.Role.MEMBER;
        IRMS.Role actualRole = irms.getAnalystRole("Admin");
        assertEquals(expectedRole, actualRole);
        irms.submitIncident("Admin", "0", 1);
        boolean expectedSubmitIncidentCheck = true;
        boolean actualSubmitIncidentCheck = irms.isSavedIncident("0", 1);
        assertEquals(expectedSubmitIncidentCheck, actualSubmitIncidentCheck);
        irms.getIncident("Admin",1);
    }
}
