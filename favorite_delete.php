<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');

    //POST 값을 읽어온다.
    $item_name = isset($_POST['item_name']) ? $_POST['item_name'] : '';
    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    //mysql의 favorite 테이블에서 item_name이 같은 데이터를 검색한다.
    $sql="select * from favorite where item_name='$item_name'";
    $stmt = $con->prepare($sql);
    $stmt->execute();

    try{
        //mysql의 favorite 테이블에서 item_name이 같은 데이터를 찾아 삭제한다.
        $sql = "delete from favorite where item_name='$item_name'";
        $stmt = $con->prepare($sql);
        $stmt->execute();
    } catch(PDOException $e) {
        die("Database error: " . $e->getMessage()); 
    }
    
?>

<?php 
    if (isset($errMSG)) echo $errMSG;
    if (isset($successMSG)) echo $successMSG;

 $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");
   
    if( !$android )
    {
?>
    <html>
       <body>
            <form action="<?php $_PHP_SELF ?>" method="POST">
                name: <input type = "text" name = "name" />
                content: <input type = "text" name = "content" />
                <input type = "submit" name = "submit" />
            </form>
       
       </body>
    </html>

<?php 
    }
?>
