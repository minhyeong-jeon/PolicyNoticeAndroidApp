<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');

    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android"); //유저의 브라우저 접속환경 파악


    // 안드로이드 코드의 postParameters 변수에 적어준 이름을 가지고 값을 전달 받는다

    $userID = $_POST['userID'];
    $userPass = $_POST['userPass'];
    $userLifearray=$_POST['userLifearray'];
    $userTrgterIndvdl=$_POST['userTrgterIndvdl'];
    $userArea=$_POST['userArea'];

    $sql="select * from user where userID='$userID'"; //user테이블에서 userID를 조건으로하여 모든 컬럼 조회
    $stmt = $con->prepare($sql); 
    $stmt->execute();

    $hash = password_hash($_POST['userPass'],PASSWORD_BCRYPT); //$userPass가 가지고있는 비밀번호 암호화

    //수정할 비밀번호가 입력되어 있으면
    if($userPass != "" ){
        if ($stmt->rowCount() == 0){
            echo "일치하는 정보가 없습니다.";
        }

        else{
            try{
                // user의 변경된 정보 update
                $sql = "update user set userPass='$hash', userLifearray='$userLifearray', 
                        userTrgterIndvdl='$userTrgterIndvdl', userArea='$userArea' where userID='$userID'";
                $stmt = $con->prepare($sql);
                $stmt->execute();

                //SQL 실행결과를 위한 메시지 생성
                if($stmt->execute())
                {
                    $successMSG = "수정되었습니다";
                }
                else
                {
                    $errMSG = "사용자 수정 에러";
                }

            } 
            catch(PDOException $e) {
                die("Database error: " . $e->getMessage()); 
            }
        }
    }

     //수정할 비밀번호가 입력되어 있지않으면
    else{
        if ($stmt->rowCount() == 0){
            echo "일치하는 정보가 없습니다.";
        }
        else{
            while(($row=$stmt->fetch(PDO::FETCH_ASSOC))){
                $userPass=$row['userPass']; //DB에서 userPass를 가져옴
        
                try{
                    // SQL문을 실행하여 데이터를 MySQL 서버의 user 테이블에 저장
                    $sql = "update user set userPass='$userPass', userLifearray='$userLifearray', userTrgterIndvdl='$userTrgterIndvdl', userArea='$userArea' where userID='$userID'";
                    $stmt = $con->prepare($sql);
                    $stmt->execute();
    
                    //SQL 실행결과를 위한 메시지 생성
                    if($stmt->execute())
                    {
                        $successMSG = "수정되었습니다";
                    }
                    else
                    {
                        $errMSG = "사용자 수정 에러";
                    }
    
                } 
                catch(PDOException $e) {
                    die("Database error: " . $e->getMessage()); 
                }
            }
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