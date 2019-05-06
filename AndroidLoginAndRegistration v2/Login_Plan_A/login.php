<?php

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

require_once 'DB_Functions.php';
$db = new DB_Functions();


$response = array("error" => FALSE);

if (isset($_POST['uname']) && isset($_POST['password'])) {

    
    $email = $_POST['uname'];
    $password = $_POST['password'];

    
    $user = $db->getUserByUsernameAndPassword($uname, $password);

    if ($user != false) {
        
        $response["error"] = FALSE;
        $response["uid"] = $user["unique_id"];
        $response["user"]["fname"] = $user["fname"];
        $response["user"]["lname"] = $user["lname"];
        $response["user"]["uname"] = $user["uname"];
        $response["user"]["created_at"] = $user["created_at"];
        $response["user"]["updated_at"] = $user["updated_at"];
        echo json_encode($response);
    } else {
        
        $response["error"] = TRUE;
        $response["error_msg"] = "Login credentials are wrong. Please try again!";
        echo json_encode($response);
    }
} else {

    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters email or password is missing!";
    echo json_encode($response);
}
?>

