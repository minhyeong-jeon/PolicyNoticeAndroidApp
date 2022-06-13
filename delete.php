<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');

    //POST 값을 읽어온다.
    $userID = isset($_POST['userID']) ? $_POST['userID'] : '';
    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    $sql="select * from user where userID='$userID'"; //user테이블에서 userID를 조건으로하여 모든 컬럼 조회
    $stmt = $con->prepare($sql);
    $stmt->execute();

    if ($stmt->rowCount() == 0){

        echo $userID,",";
        echo "일치하는 정보가 없습니다.";
    }

    else{
        try{
            //회원탈퇴 시 해당 userID와 관련된 모든 정보를 각 테이블에서 지우기 위해 LEFT JOIN 문법 사용
            $sql = "DELETE user, favorite, event, inquiry
                    FROM user
                    LEFT JOIN favorite ON favorite.userID = user.userID
                    LEFT JOIN event ON event.userID = user.userID
                    LEFT JOIN inquiry ON inquiry.userID = user.userID
                    WHERE user.userID = '$userID'";

            $stmt = $con->prepare($sql);
            $stmt->execute();
            

            //SQL 실행결과를 위한 메시지 생성
            if($stmt->execute())
            {
                $successMSG = "탈퇴하였습니다.";
            }
            else
            {
                $errMSG = "탈퇴 에러";
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