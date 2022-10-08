<?php 
    error_reporting(E_ALL); 
    ini_set('display_errors',1); 
    include('dbcon.php');
    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {

        // 안드로이드 코드의 postParameters 변수에 적어준 이름을 가지고 값을 전달 받는다.
        $userID=$_POST['userID'];
        $type=$_POST['type'];
        $title=$_POST['title'];
        $email=$_POST['email'];
        $content=$_POST['content'];

        //제목이 입력되지 않은 경우
        if(empty($title)){
            $errMSG = "제목을 입력하세요.";
        }
        //이메일이 입력되지 않은 경우
        else if(empty($email)){
            $errMSG = "이메일을 입력하세요.";
        }
        //내용이 입력되지 않은 경우
        else if(empty($content)){
            $errMSG = "내용을 입력하세요.";
        }
        //제목, 이메일, 내용이 모두 입력 되었을 경우
        if(!isset($errMSG))
        {
            //mysql의 inquiry 테이블에서 userID가 같은 데이터를 검색한다.
            $sql="select * from inquiry where userID='$userID'";
            $stmt = $con->prepare($sql);
            $stmt->execute();

            try{
                // SQL문을 실행하여 데이터를 MySQL 서버의 person 테이블에 저장한다.
                $stmt = $con->prepare('INSERT INTO inquiry(userID, type, title, email, content) VALUES(:userID, :type, :title, :email, :content)');
                $stmt->bindParam(':userID', $userID);
                $stmt->bindParam(':type', $type);
                $stmt->bindParam(':title', $title);
                $stmt->bindParam(':email', $email);
                $stmt->bindParam(':content', $content);

                //SQL 실행결과를 위한 메시지를 생성한다.
                if($stmt->execute())
                {
                    $successMSG = "문의가 성공적으로 등록되었습니다.";
                }
                else
                {
                    $errMSG = "문의 추가 에러";
                }

            } catch(PDOException $e) {
                die("Database error: " . $e->getMessage()); 
            }
        }
    }
?>

<?php 
    if (isset($errMSG)) echo $errMSG;
    if (isset($successMSG)) echo $successMSG;

 $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");
   
    if( !$android )
    {}
?>
