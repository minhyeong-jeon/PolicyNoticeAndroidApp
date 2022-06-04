<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');

    //POST 값을 읽어온다.
    //$userPass=isset($_POST['userPass']) ? $_POST['userPass'] : '';
    $userID = isset($_POST['userID']) ? $_POST['userID'] : '';
    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    $sql="select * from user where userID='$userID'";
    $stmt = $con->prepare($sql);
    $stmt->execute();

    if ($stmt->rowCount() == 0){

        //echo "'";
        echo $userID,",";
        echo "일치하는 정보가 없습니다.";
    }

    else{
        try{
            // $sql = "delete from user,favorite where userID='$userID'";
            $sql = "delete from a,b using user as a left join favorite as b on a.userID=B.userID where b.userID='$userID'";

            $stmt = $con->prepare($sql);
            $stmt->execute();
            

            //SQL 실행결과를 위한 메시지 생성
            if($stmt->execute())
            {
                $successMSG = "탈퇴하였습니다.";
            }
            else
            {
                $errMSG = "사용자 삭제 에러";
            }

        } catch(PDOException $e) {
            die("Database error: " . $e->getMessage()); 
        }
    }

?>

<?php 
    if (isset($errMSG)) echo $errMSG;
    if (isset($successMSG)) echo $successMSG;

 $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");
   
    if( !$android )
    {
?>

<?php 
    }
?>