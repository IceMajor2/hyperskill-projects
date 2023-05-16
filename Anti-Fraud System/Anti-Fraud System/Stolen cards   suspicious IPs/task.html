<div class="step-text">
<h5 id="description">Description</h5>
<p>Usually, hackers use certain IP addresses. As a result, you should be cautious about activities coming from these addresses. In addition, card numbers can be reported as stolen. The anti-fraud system should prohibit all transactions with stolen cards.</p>
<p>In this stage, we need to enable our anti-fraud system to retrieve a list of prohibited card numbers and suspicious IP addresses to ban them from carrying out any transactions.</p>
<p>In our service, we will check IP addresses for compliance with IPv4. Any address following this format consists of four series of numbers from <code class="language-json">0</code> to <code class="language-json">255</code> separated by dots. Here is an example of a valid IP address: <code class="language-json">132.245.4.216</code></p>
<p>Card numbers must be checked according to the <strong>Luhn</strong> <strong>algorithm</strong>. During testing, we will use the following card format: <code class="language-json">4000008449433403</code>.</p>
<ul>
<li>The first six digits are the <strong>Issuer Identification Number </strong>(IIN). The first digit is the <strong>Major Industry Identifier </strong>(MII);</li>
<li>The seventh to second-to-last digits indicate the <strong>customer account number</strong>;</li>
<li>The last digit is the<strong> check digit</strong> (or checksum). It validates the credit card number using the Luhn algorithm.</li>
</ul>
<p>So, 16 digits in total.</p>
<p><b>Tip:</b> If you want to know more about Luhn algorithms, take a look at the <a href="https://en.wikipedia.org/wiki/Luhn_algorithm" rel="noopener noreferrer nofollow" target="_blank">Wikipedia page</a> of the same name or complete the <a href="https://hyperskill.org/projects/93" rel="noopener noreferrer nofollow" target="_blank">Simple Banking System</a> project on Hyperskill.</p>
<h5 id="objectives">Objectives</h5>
<ul>
<li>Change the role model:</li>
</ul>
<table border="1" cellpadding="1" cellspacing="1">
<tbody>
<tr>
<td> </td>
<td>Anonymous</td>
<td>MERCHANT</td>
<td>ADMINISTRATOR</td>
<td>SUPPORT</td>
</tr>
<tr>
<td>POST api/auth/user</td>
<td>+</td>
<td>+</td>
<td>+</td>
<td>+</td>
</tr>
<tr>
<td>DELETE api/auth/user</td>
<td>-</td>
<td>-</td>
<td>+</td>
<td>-</td>
</tr>
<tr>
<td>GET api/auth/list</td>
<td>-</td>
<td>-</td>
<td>+</td>
<td>+</td>
</tr>
<tr>
<td>POST api/antifraud/transaction</td>
<td>-</td>
<td>+</td>
<td>-</td>
<td>-</td>
</tr>
<tr>
<td>POST, DELETE, GET api/antifraud/suspicious-ip</td>
<td>-</td>
<td>-</td>
<td>-</td>
<td>+</td>
</tr>
<tr>
<td>POST, DELETE, GET api/antifraud/stolencard</td>
<td>-</td>
<td>-</td>
<td>-</td>
<td>+</td>
</tr>
</tbody>
</table>
<ul>
<li>Add the <span style="color: #ff4363;"><code class="language-json">POST /api/antifraud/suspicious-ip</code></span> endpoint that saves suspicious IP addresses to the database. It must accept the following JSON body:</li>
</ul>
<pre><code class="language-json">{
   "ip": "&lt;String value, not empty&gt;"
}</code></pre>
<p>If successful, respond with the <code class="language-json">HTTP Created</code> status (<code class="language-json">200</code>) and the body like this:</p>
<pre><code class="language-json">{
   "id": "&lt;Long value, not empty&gt;",
   "ip": "&lt;String value, not empty&gt;"
}</code></pre>
<p>If an IP is already in the database, respond with the <code class="language-json">HTTP Conflict</code> status (<code class="language-json">409</code>).</p>
<p>If an IP address has the wrong format, respond with the <code class="language-json">HTTP Bad Request</code> status (<code class="language-json">400</code>).</p>
<ul>
<li>Add the <span style="color: #ff4363;"><code class="language-json">DELETE /api/antifraud/suspicious-ip/{ip}</code></span> endpoint that deletes IP addresses from the database. If IP addresses are deleted successfully, respond with the <code class="language-json">HTTP OK</code> status (<code class="language-json">200</code>) and the body like this:</li>
</ul>
<pre><code class="language-json">{
   "status": "IP &lt;ip address&gt; successfully removed!"
}</code></pre>
<p>If an IP is not found in the database, respond with the <code class="language-json">HTTP Not Found</code> status (<code class="language-json">404</code>).</p>
<p>If an IP address has the wrong format (not following the Description section rules), respond with the <code class="language-json">HTTP Bad Request</code> status (<code class="language-json">400</code>)</p>
<ul>
<li>Add the <span style="color: #ff4363;"><code class="language-json">GET /api/antifraud/suspicious-ip</code></span> endpoint that shows IP addresses in the database. Endpoint must respond with the <code class="language-json">HTTP OK</code> status (<code class="language-json">200</code>) and body with an array of JSON objects representing IP address sorted by ID in <strong>ascending</strong> order (or an empty array if the database is empty):</li>
</ul>
<pre><code class="language-json">[
    {
        "id": 1,
        "ip": "192.168.1.1"
    },
     ...
    {
        "id": 100,
        "ip": "192.168.1.254"
    }
]</code></pre>
<p>or</p>
<pre><code class="language-json">[]</code></pre>
<ul>
<li>Add the <span style="color: #ff4363;"><code class="language-json">POST /api/antifraud/stolencard</code></span> endpoint that saves stolen card data in the database. It must accept the following JSON body:</li>
</ul>
<pre><code class="language-json">{
   "number": "&lt;String value, not empty&gt;"
}</code></pre>
<p>If successful, respond with the <code class="language-json">HTTP Created</code> status (<code class="language-json">200</code>) and the following body:</p>
<pre><code class="language-json">{
   "id": "&lt;Long value, not empty&gt;",
   "number": "&lt;String value, not empty&gt;"
}</code></pre>
<p>If the card number is already in the database, respond with the <code class="language-json">HTTP Conflict</code> status (<code class="language-json">409</code>).</p>
<p>If a card number has the wrong format, respond with the <code class="language-json">HTTP Bad Request</code> status (<code class="language-json">400</code>).</p>
<ul>
<li>Add the <span style="color: #ff4363;"><code class="language-json">DELETE /api/antifraud/stolencard/{number}</code></span> endpoint that deletes a card number from the database. If a card number has been deleted, respond with the <code class="language-json">HTTP OK</code> status (<code class="language-json">200</code>) and the body below:</li>
</ul>
<pre><code class="language-json">{
   "status": "Card &lt;number&gt; successfully removed!"
}</code></pre>
<p>If a card number is not found in the database, respond with the <code class="language-json">HTTP Not Found</code> status (<code class="language-json">404</code>).</p>
<p>If a card number follows the wrong format (look at the Description section), respond with <code class="language-json">HTTP Bad Request</code> status (<code class="language-json">400</code>).</p>
<ul>
<li>Add the <span style="color: #ff4363;"><code class="language-json">GET /api/antifraud/stolencard</code></span> endpoint that shows card numbers stored in the database. The endpoint must respond with the <code class="language-json">HTTP OK </code>status (<code class="language-json">200</code>) and a body with an array of JSON objects that display the card numbers sorted by ID in <strong>ascending</strong> <strong>order</strong> (or an empty array if the database is empty):</li>
</ul>
<pre><code class="language-json">[
    {
        "id": 1,
        "number": "4000008449433403"
    },
     ...
    {
        "id": 100,
        "number": "4000009455296122"
    }
]</code></pre>
<ul>
<li>Change the  <code class="language-json">POST /api/antifraud/transaction</code>, now endpoint must accept data in the following JSON format:</li>
</ul>
<pre><code class="language-json">{
  "amount": &lt;Long&gt;,
  "ip": "&lt;String value, not empty&gt;",
  "number": "&lt;String value, not empty&gt;"
}</code></pre>
<ul>
<li>Implement the following rules in addition to the previous ones:</li>
</ul>
<ol>
<li>If an IP address is in the blacklist, the transaction is <code class="language-json">PROHIBITED</code>;</li>
<li>If a card number is in the blacklist, the transaction is <code class="language-json">PROHIBITED</code>.</li>
</ol>
<ul>
<li>If the validation process was successful, the endpoint should respond with the status <code class="language-json">HTTP OK </code>(<code class="language-json">200</code>) and return the following JSON: </li>
</ul>
<pre><code class="language-json">{
  "result": &lt;String&gt;,
  "info": &lt;String&gt;
}</code></pre>
<p>If the <code class="language-json">result</code> is <code class="language-json">ALLOWED</code>, the <code class="language-json">info</code> field must be set to <code class="language-json">none</code> (a string value).</p>
<p>In the case of <code class="language-json">PROHIBITED</code> or <code class="language-json">MANUAL_PROCESSING</code>, the <code class="language-json">info</code> field must contain the reason for rejecting the transaction; the reason must be separated by <code class="language-json">,</code> and sorted alphabetically. For example, <code class="language-json">amount, card-number, ip</code>.</p>
<p>If a request contains wrong data, an IP address and a card number must be validated as described in the Description section, the endpoint should respond with the status <code class="language-json">HTTP Bad Request </code>(<code class="language-json">400</code>).</p>
<h5 id="examples">Examples</h5>
<p><strong>Example 1: </strong><em>a POST request for /api/antifraud/transaction</em></p>
<p><em>Request body:</em></p>
<pre><code class="language-json">{
  "amount": 150,
  "ip": "192.168.1.1",
  "number": "4000008449433403"
}</code></pre>
<p><em>Response: </em><code class="language-json">200 OK</code></p>
<p><em>Response body:</em></p>
<pre><code class="language-json">{
   "result": "ALLOWED",
   "info": "none"
}</code></pre>
<p><strong>Example 2: </strong><em>a POST request for </em>/api/antifraud/transaction</p>
<p><em>Request body:</em></p>
<pre><code class="language-json">{
  "amount": 800,
  "ip": "192.168.1.1",
  "number": "4000008449433403"
}</code></pre>
<p><em>Response: </em><code class="language-json">200 OK</code></p>
<p><em>Response body:</em></p>
<pre><code class="language-json">{
   "result": "MANUAL_PROCESSING",
   "info": "amount"
}</code></pre>
<p><strong>Example 3: </strong><em>a POST request for /api/antifraud/transaction; the IP address is in the black list</em></p>
<p><em>Request body:</em></p>
<pre><code class="language-json">{
  "amount": 1800,
  "ip": "192.168.1.1",
  "number": "4000008449433403"
}</code></pre>
<p><em>Response: </em><code class="language-json">200 OK</code></p>
<p><em>Response body:</em></p>
<pre><code class="language-json">{
   "result": "PROHIBITED",
   "info": "amount, ip"
}</code></pre>
<p><strong>Example 4: </strong><em>a POST request for /api/antifraud/suspicious-ip</em></p>
<p><em>Request body:</em></p>
<pre><code class="language-json">{
   "ip": "192.168.1.1"
}</code></pre>
<p><em>Response: </em><code class="language-json">200 OK</code></p>
<p><em>Response body:</em></p>
<pre><code class="language-json">{
   "id": 1,
   "ip": "192.168.1.1"
}</code></pre>
<p><strong>Example 5: </strong><em>a POST request for /api/antifraud/suspicious-ip; wrong IP format</em></p>
<p><em>Request body:</em></p>
<pre><code class="language-json">{
   "ip": "192.168.356.1"
}</code></pre>
<p><em>Response: <code class="language-json"><span style="color: #000000;">400 BAD REQUEST</span></code></em></p>
<p><strong>Example 6: </strong><em>a POST request for /api/antifraud/stolencard</em></p>
<p><em>Request body:</em></p>
<pre><code class="language-json">{
   "number": "4000008449433403"
}</code></pre>
<p><em>Response: </em><code class="language-json">200 OK</code></p>
<p><em>Response body:</em></p>
<pre><code class="language-json">{
   "id": 1,
   "number": "4000008449433403"
}</code></pre>
<p><strong>Example 7: </strong><em>a POST request for /api/antifraud/stolencard; the card number is not verified by the Luhn algorithm</em></p>
<p><em>Request body:</em></p>
<pre><code class="language-json">{
   "number": "4000008449433402"
}</code></pre>
<p><em>Response: <code class="language-json"><span style="color: #000000;">400 BAD REQUEST</span></code></em></p>
<p><strong>Example 8: </strong><em>a POST request for /api/antifraud/transaction; the card number is not verified by the Luhn algorithm</em></p>
<p><em>Request body:</em></p>
<pre><code class="language-json">{
  "amount": 800,
  "ip": "192.168.1.1",
  "number": "4000008449433402"
}</code></pre>
<p><em>Response: <code class="language-json"><span style="color: #000000;">400 BAD REQUEST</span></code></em></p>
<p><strong>Example 9: </strong><em>a DELETE request for /api/antifraud/stolencard/4000008449433403</em></p>
<p><em>Response: </em><code class="language-json">200 OK</code></p>
<p><em>Response body:</em></p>
<pre><code class="language-json">{
   "status": "Card 4000008449433403 successfully removed!"
}</code></pre>
</div>