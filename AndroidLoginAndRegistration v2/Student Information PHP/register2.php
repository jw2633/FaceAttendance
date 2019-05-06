<?php

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

require_once 'DB_Functions2.php';
$db = new DB_Functions2();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['fname']) && isset($_POST['lname']) && isset($_POST['uname'])) {

    // receiving the post params
    $fname = $_POST['fname'];
    $lname = $_POST['lname'];
    $uname = $_POST['uname'];
    //$password = $_POST['password'];

    // check if user is already existed with the same email
    if ($db->isUserExisted($uname)) {
        // user already existed
        $response["error"] = TRUE;
        $response["error_msg"] = "User already existed with " . $uname;
        echo json_encode($response);
    } else {
        // create a new user
        $user = $db->storeUser($fname, $lname, $uname);
        if ($user) {
            // user stored successfully
            $response["error"] = FALSE;
            $response["uid"] = $user["unique_id"];
            $response["user"]["name"] = $user["fname"];
            $response["user"]["uname"] = $user["uname"];
            $response["user"]["created_at"] = $user["created_at"];
            $response["user"]["updated_at"] = $user["updated_at"];
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in registration!";
            echo json_encode($response);
        }
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (fname, lname, or uname) is missing!";
    echo json_encode($response);
}
?>

