<?php

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

require_once 'DB_Functions2.php';
$db = new DB_Functions2();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['uname']) && isset($_POST['password'])) {

    // receiving the post params
    $email = $_POST['uname'];
    $password = $_POST['password'];

    // get the user by username and password
    $user = $db->getUserByUsernameAndPassword($uname, $password);

    if ($user != false) {
        // use is found
        $response["error"] = FALSE;
        $response["uid"] = $user["unique_id"];
        $response["user"]["fname"] = $user["fname"];
        $response["user"]["lname"] = $user["lname"];
        $response["user"]["uname"] = $user["uname"];
        $response["user"]["created_at"] = $user["created_at"];
        $response["user"]["updated_at"] = $user["updated_at"];
        echo json_encode($response);
    } else {
        // user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "Login credentials are wrong. Please try again!";
        echo json_encode($response);
    }
} else {
    // required post params is missing
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters email or password is missing!";
    echo json_encode($response);
}
?>

