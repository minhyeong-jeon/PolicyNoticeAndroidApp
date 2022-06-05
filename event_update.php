<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');

    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android"); //유저의 브라우저 접속환경 파악


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
   {

        // 안드로이드 코드의 postParameters 변수에 적어준 이름을 가지고 값을 전달 받는다
        $title=$_POST['title'];
        $startdate=$_POST['startdate'];
        $enddate=$_POST['enddate'];
        $passedEventID=$_POST['passedEventID'];

        $sql="select * from event where ID='$passedEventID'";
        $stmt = $con->prepare($sql);
        $stmt->execute();


        try{
            // event 변경된 정보 update
            $sql = "update event set title='$title', 
                                    startdate='$startdate', enddate='$enddate' where ID='$passedEventID'";
            $stmt = $con->prepare($sql);
            $stmt->execute();

            //SQL 실행결과를 위한 메시지 생성
            if($stmt->execute())
            {
                $successMSG = "수정되었습니다";
            }
            else
            {
                $errMSG = "이벤트 수정 에러";
            }

        } 
        catch(PDOException $e) {
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