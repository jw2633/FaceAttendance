<?php

class DB_Functions2 {

    private $conn;

    // constructor
    function __construct() {
        require_once 'DB_Connect2.php';
        // connecting to database
        $db = new Db_Connect();
        $this->conn = $db->connect();
    }

    // destructor
    function __destruct() {
        
    }

    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($fname, $lname, $uname) {
        $uuid = uniqid('', true);
        //$hash = $this->hashSSHA($password);
        //$encrypted_password = $hash["encrypted"]; // encrypted password
        //$salt = $hash["salt"];

        $stmt = $this->conn->prepare("INSERT INTO students(unique_id, fname, lname, uname, created_at) VALUES(?,?, ?, ?, NOW())");
        $stmt->bind_param("ssss", $uuid, $fname, $lname, $uname);
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM students WHERE uname = ?");
            $stmt->bind_param("s", $uname);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            return $user;
        } else {
            return false;
        }
    }

    /**
     * Get user by username and password
     
    public function getUserByUsernameAndPassword($uname, $password) {

        $stmt = $this->conn->prepare("SELECT * FROM students WHERE uname = ?");

        $stmt->bind_param("s", $uname);

        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            // verifying user password
            $salt = $user['salt'];
            $encrypted_password = $user['encrypted_password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // check for password equality
            if ($encrypted_password == $hash) {
                // user authentication details are correct
                return $user;
            }
        } else {
            return NULL;
        }
    }
*/
    /**
     * Check user is existant or not
     */
    public function isUserExisted($uname) {
        $stmt = $this->conn->prepare("SELECT uname from students WHERE uname = ?");

        $stmt->bind_param("s", $uname);

        $stmt->execute();

        $stmt->store_result();

        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }

    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     
    public function hashSSHA($password) {

        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }

    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     
    public function checkhashSSHA($salt, $password) {

        $hash = base64_encode(sha1($password . $salt, true) . $salt);

        return $hash;
    }
*/
}

?>
