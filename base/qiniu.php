<?php

//同一目录下不需要导入文件
include 'config.php';

$qiniu = new Qiniu();
$result = array("token" => $qiniu->uploadToken());
echo json_encode($result);

class Qiniu
{

    public function sign($data)
    {
        $hmac = hash_hmac('sha1', $data, Config::$QINIU_SECRET_KEY, true);
        return Config::$QINIU_ACCESS_KEY . ':' . $this->base64_urlSafeEncode($hmac);
    }

    public function signWithData($data)
    {
        $data = $this->base64_urlSafeEncode($data);
        return $this->sign($data) . ':' . $data;
    }

    public function signRequest($urlString, $body, $contentType = null)
    {
        $url = parse_url($urlString);
        $data = '';
        if (isset($url['path'])) {
            $data = $url['path'];
        }
        if (isset($url['query'])) {
            $data .= '?' . $url['query'];
        }
        $data .= "\n";

        if ($body != null &&
            ($contentType == 'application/x-www-form-urlencoded') ||  $contentType == 'application/json') {
            $data .= $body;
        }
        return $this->sign($data);
    }

    public function verifyCallback($contentType, $originAuthorization, $url, $body)
    {
        $authorization = 'QBox ' . $this->signRequest($url, $body, $contentType);
        return $originAuthorization === $authorization;
    }

    public function uploadToken($key = null,$expires = 600,$policy = null,$strictPolicy = true) {
        $deadline = time() + $expires;
        $scope = 'shadow';
        if ($key != null) {
            $scope .= ':' . $key;
        }
        $args = array('insertOnly'=>1);
        //$args = self::copyPolicy($args, $policy, $strictPolicy);
        $args['scope'] = $scope;
        $args['deadline'] = $deadline;
        $b = json_encode($args);
        return $this->signWithData($b);
    }

    /**
     *上传策略，参数规格详见
     *http://developer.qiniu.com/docs/v6/api/reference/security/put-policy.html
     */
    private static $policyFields = array(
        'callbackUrl',
        'callbackBody',
        'callbackHost',
        'callbackBodyType',
        'callbackFetchKey',

        'returnUrl',
        'returnBody',

        'endUser',
        'saveKey',
        'insertOnly',

        'detectMime',
        'mimeLimit',
        'fsizeLimit',

        'persistentOps',
        'persistentNotifyUrl',
        'persistentPipeline',
    );

    private static function copyPolicy(&$policy, $originPolicy, $strictPolicy)
    {
        if ($originPolicy == null) {
            return;
        }
        foreach ($originPolicy as $key => $value) {
            if (in_array($key, self::$deprecatedPolicyFields)) {
                throw new \InvalidArgumentException("{$key} has deprecated");
            }
            if (!$strictPolicy || in_array($key, self::$policyFields)) {
                $policy[$key] = $value;
            }
        }
        return $policy;
    }

    public function authorization($url, $body = null, $contentType = null)
    {
        $authorization = 'QBox ' . $this->signRequest($url, $body, $contentType);
        return array('Authorization' => $authorization);
    }

    function base64_urlSafeEncode($data)
    {
        $find = array('+', '/');
        $replace = array('-', '_');
        return str_replace($find, $replace, base64_encode($data));
    }
}
