<?php

require_once 'DB_Functions.php';
$db = new DB_Functions();


$response = array("error" => FALSE);

if (isset($_POST['fname']) && isset($_POST['lname']) && isset($_POST['uname']) && isset($_POST['password'])) {

    
    $fname = $_POST['fname'];
    $lname = $_POST['lname'];
    $uname = $_POST['uname'];
    $password = $_POST['password'];

    
    if ($db->isUserExisted($uname)) {
        
        $response["error"] = TRUE;
        $response["error_msg"] = "User already existed with " . $uname;
        echo json_encode($response);
    } else {
        
        $user = $db->storeUser($fname, $lname, $uname, $password);
        if ($user) {
            
            $response["error"] = FALSE;
            $response["uid"] = $user["unique_id"];
            $response["user"]["name"] = $user["fname"];
            $response["user"]["uname"] = $user["uname"];
            $response["user"]["created_at"] = $user["created_at"];
            $response["user"]["updated_at"] = $user["updated_at"];
            echo json_encode($response);
        } else {
            
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in registration!";
            echo json_encode($response);
        }
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (fname, lname, uname, or password) is missing!";
    echo json_encode($response);
}
?>

