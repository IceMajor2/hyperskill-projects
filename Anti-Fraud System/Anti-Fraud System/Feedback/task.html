<div class="step-text">
<h5 id="description">Description</h5>
<p>When working on an anti-fraud system, it is necessary to consider that the environment of transactions is constantly changing. There are many factors like the country's economy, the behavior of fraudsters, and the number of transactions happening concurrently that influence what we can call fraud. It is necessary to add certain adaptation mechanisms to our service, such as <strong>feedback</strong>. Feedback will be carried out manually by a <code class="language-json">SUPPORT</code> specialist for completed transactions. Based on the feedback results, we will change the limits of fraud detection algorithms following the special rules. Take a look at the table below that shows the logic of our feedback system:</p>
<table border="1" cellpadding="1" cellspacing="1" style="width: 700px;">
<tbody>
<tr>
<th>
<table>
<tbody>
<tr>
<th>Transaction Feedback →</th>
</tr>
<tr>
<th style="text-align: left;">Transaction Validity ↓</th>
</tr>
</tbody>
</table>
</th>
<th>ALLOWED</th>
<th>MANUAL_PROCESSING</th>
<th>PROHIBITED</th>
</tr>
<tr>
<th>ALLOWED</th>
<td>Exception</td>
<td>↓ max ALLOWED</td>
<td>
<p>↓ max ALLOWED</p>
<p>↓ max MANUAL</p>
</td>
</tr>
<tr>
<th>MANUAL_PROCESSING</th>
<td>↑ max ALLOWED</td>
<td>Exception</td>
<td>↓ max MANUAL</td>
</tr>
<tr>
<th>PROHIBITED</th>
<td>
<p>↑ max ALLOWED</p>
<p>↑ max MANUAL</p>
</td>
<td>↑ max MANUAL</td>
<td>Exception</td>
</tr>
</tbody>
</table>
<p>The formula for increasing the limit:</p>
<pre><code class="language-json">new_limit = 0.8 * current_limit + 0.2 * value_from_transaction
</code></pre>
<p>The formula for decreasing the limit:</p>
<pre><code class="language-json">new_limit = 0.8 * current_limit - 0.2 * value_from_transaction
</code></pre>
<p>If the new value is fractional, <strong>round</strong> <strong>it</strong> <strong>up</strong> (use <code class="language-json">ceil</code>).</p>
<p>Let's take an example:</p>
<p>Current <code class="language-json">max ALLOWED</code> limit is <code class="language-json">200</code>. A transaction of <code class="language-json">210</code> is validated for <code class="language-json">MANUAL_PROCESSING</code>. The feedback has been received that a transaction with a value <code class="language-json">210</code> is <code class="language-json">ALLOWED</code>. Max <code class="language-json">ALLOWED</code> value must be updated to <code class="language-json">202</code>:</p>
<pre><code class="language-json">202 = 0.8 * 200 + 0.2 * 210
</code></pre>
<p>Consider the following limitations for feedback:</p>
<ul>
<li>For each transaction, only one feedback is allowed.</li>
<li>Feedback is provided only by users with <code class="language-json">SUPPORT</code> role.</li>
</ul>
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
<td>POST /api/auth/user</td>
<td>+</td>
<td>+</td>
<td>+</td>
<td>+</td>
</tr>
<tr>
<td>DELETE /api/auth/user</td>
<td>-</td>
<td>-</td>
<td>+</td>
<td>-</td>
</tr>
<tr>
<td>GET /api/auth/list</td>
<td>-</td>
<td>-</td>
<td>+</td>
<td>+</td>
</tr>
<tr>
<td>POST /api/antifraud/transaction</td>
<td>-</td>
<td>+</td>
<td>-</td>
<td>-</td>
</tr>
<tr>
<td>/api/antifraud/suspicious-ip</td>
<td>-</td>
<td>-</td>
<td>-</td>
<td>+</td>
</tr>
<tr>
<td>/api/antifraud/stolencard</td>
<td>-</td>
<td>-</td>
<td>-</td>
<td>+</td>
</tr>
<tr>
<td>GET /api/antifraud/history</td>
<td>-</td>
<td>-</td>
<td>-</td>
<td>+</td>
</tr>
<tr>
<td>PUT /api/antifraud/transaction</td>
<td>-</td>
<td>-</td>
<td>-</td>
<td>+</td>
</tr>
</tbody>
</table>
<ul>
<li>Add the <code class="language-json">PUT</code> method to the <code class="language-json">/api/antifraud/transaction</code> endpoint that adds feedback for a transaction. It must accept the following JSON body:</li>
</ul>
<pre><code class="language-json">{
   "transactionId": &lt;Long&gt;,
   "feedback": "&lt;String&gt;"
}</code></pre>
<p>If successful, update the limits of transaction validation following the table above and respond with the <code class="language-json">HTTP OK</code> status (<code class="language-json">200</code>) and the following body:</p>
<pre><code class="language-json">{
  "transactionId": &lt;Long&gt;,
  "amount": &lt;Long&gt;,
  "ip": "&lt;String value, not empty&gt;",
  "number": "&lt;String value, not empty&gt;",
  "region": "&lt;String value, not empty&gt;",
  "date": "yyyy-MM-ddTHH:mm:ss",
  "result": "&lt;String&gt;",
  "feedback": "&lt;String&gt;"
}</code></pre>
<p>If the feedback for a specified transaction is already in the database, respond with the <code class="language-json">HTTP Conflict</code> status (<code class="language-json">409</code>).</p>
<p>If the feedback has the wrong format (other than <code class="language-json">ALLOWED</code>, <code class="language-json">MANUAL_PROCESSING</code>, <code class="language-json">PROHIBITED</code>), respond with the <code class="language-json">HTTP Bad Request</code> status (<code class="language-json">400</code>).</p>
<p>If the feedback throws an Exception following the table, respond with the <code class="language-json">HTTP Unprocessable Entity</code> status (<code class="language-json">422</code>).</p>
<p>If the transaction is not found in history, respond with the <code class="language-json">HTTP Not Found</code> status (<code class="language-json">404</code>).</p>
<ul>
<li>Add the <code class="language-json">GET /api/antifraud/history</code> endpoint that shows the transaction history. The endpoint must respond with the <code class="language-json">HTTP OK</code> status (<code class="language-json">200</code>) and a body with an array of JSON objects that represent transactions sorted by ID in <strong>ascending</strong> order (or an empty array if the history is empty):</li>
</ul>
<pre><code class="language-json">[
    {
      "transactionId": &lt;Long&gt;,
      "amount": &lt;Long&gt;,
      "ip": "&lt;String value, not empty&gt;",
      "number": "&lt;String value, not empty&gt;",
      "region": "&lt;String value, not empty&gt;",
      "date": "yyyy-MM-ddTHH:mm:ss",
      "result": "&lt;String&gt;",
      "feedback": "&lt;String&gt;"
    },
     ...
    {
      "transactionId": &lt;Long&gt;,
      "amount": &lt;Long&gt;,
      "ip": "&lt;String value, not empty&gt;",
      "number": "&lt;String value, not empty&gt;",
      "region": "&lt;String value, not empty&gt;",
      "date": "yyyy-MM-ddTHH:mm:ss",
      "result": "&lt;String&gt;",
      "feedback": "&lt;String&gt;"
    }
]</code></pre>
<p>or</p>
<pre><code class="language-json">[]</code></pre>
<ul>
<li>Add the <code class="language-json">GET /api/antifraud/history/{number}</code> endpoint that shows the transaction history for a specified card number. If transactions for a specified card number are found, respond with the <code class="language-json">HTTP OK</code> status (<code class="language-json">200</code>) and the following body:</li>
</ul>
<pre><code class="language-json">[
    {
      "transactionId": &lt;Long&gt;,
      "amount": &lt;Long&gt;,
      "ip": "&lt;String value, not empty&gt;",
      "number": number,
      "region": "&lt;String value, not empty&gt;",
      "date": "yyyy-MM-ddTHH:mm:ss",
      "result": "&lt;String&gt;",
      "feedback": "&lt;String&gt;"
    },
     ...
    {
      "transactionId": &lt;Long&gt;,
      "amount": &lt;Long&gt;,
      "ip": "&lt;String value, not empty&gt;",
      "number": number,
      "region": "&lt;String value, not empty&gt;",
      "date": "yyyy-MM-ddTHH:mm:ss",
      "result": "&lt;String&gt;",
      "feedback": "&lt;String&gt;"
    }
]</code></pre>
<p>If transactions for a specified card number are not found, respond with the <code class="language-json">HTTP Not Found</code> status (<code class="language-json">404</code>).</p>
<p>If a card number doesn't follow the right format like in previous stages, respond with <code class="language-json">HTTP Bad Request</code> status (<code class="language-json">400</code>).</p>
<h5 id="examples">Examples</h5>
<p></p><div class="alert alert-warning">Think of the examples as a chain of consecutive and related events.</div>
<p><strong>Example 1: </strong><em>a POST request for /api/antifraud/transaction</em></p>
<p><em>Request body:</em></p>
<pre><code class="language-json">{
  "amount": 210,
  "ip": "192.168.1.1",
  "number": "4000008449433403",
  "region": "EAP",
  "date": "2022-01-22T16:04:00"
}</code></pre>
<p><em>Response: </em><code class="language-json">200 OK</code></p>
<p><em>Response body:</em></p>
<pre><code class="language-json">{
   "result": "MANUAL_PROCESSING",
   "info": "amount"
}</code></pre>
<p><strong>Example 2: </strong><em>a GET request for /api/antifraud/history/4000008449433403</em></p>
<p><em>Response: </em><code class="language-json">200 OK</code></p>
<p><em>Response body:</em></p>
<pre><code class="language-json">[
  {
  "transactionId": 1,
  "amount": 210,
  "ip": "192.168.1.1",
  "number": "4000008449433403",
  "region": "EAP",
  "date": "2022-01-22T16:04:00",
  "result": "MANUAL_PROCESSING",
  "feedback": ""
  }
]</code></pre>
<p><strong>Example 3: </strong><em>a PUT request for </em>/api/antifraud/transaction</p>
<p><em>Request body:</em></p>
<pre><code class="language-json">{
   "transactionId": 1,
   "feedback": "ALLOWED"
}</code></pre>
<p><em>Response: </em><code class="language-json">200 OK</code></p>
<p><em>Response body:</em></p>
<pre><code class="language-json">{
  "transactionId": 1,
  "amount": 210,
  "ip": "192.168.1.1",
  "number": "4000008449433403",
  "region": "EAP",
  "date": "2022-01-22T16:04:00",
  "result": "MANUAL_PROCESSING",
  "feedback": "ALLOWED"
}</code></pre>
<p><strong>Example 4: </strong><em>a POST request for /api/antifraud/transaction</em></p>
<p><em>Request body:</em></p>
<pre><code class="language-json">{
  "amount": 100,
  "ip": "192.168.1.1",
  "number": "4000008449433403",
  "region": "EAP",
  "date": "2022-01-22T16:05:00"
}</code></pre>
<p><em>Response: </em><code class="language-json">200 OK</code></p>
<p><em>Response body:</em></p>
<pre><code class="language-json">{
   "result": "ALLOWED",
   "info": "none"
}</code></pre>
<p><strong>Example 5: </strong><em>a GET request for /api/antifraud/history/4000008449433403</em></p>
<p><em>Response: </em><code class="language-json">200 OK</code></p>
<p><em>Response body:</em></p>
<pre><code class="language-json">[
  {
  "transactionId": 1,
  "amount": 210,
  "ip": "192.168.1.1",
  "number": "4000008449433403",
  "region": "EAP",
  "date": "2022-01-22T16:04:00",
  "result": "MANUAL_PROCESSING",
  "feedback": "ALLOWED"
  },
  {
  "transactionId": 2,
  "amount": 210,
  "ip": "192.168.1.1",
  "number": "4000008449433403",
  "region": "EAP",
  "date": "2022-01-22T16:05:00",
  "result": "ALLOWED",
  "feedback": ""
  }
]</code></pre>
<p><strong>Example 6: </strong><em>a PUT request for </em>/api/antifraud/transaction</p>
<p><em>Request body:</em></p>
<pre><code class="language-json">{
   "transactionId": 2,
   "feedback": "ALLOWED"
}</code></pre>
<p><em>Response: </em><code class="language-json">422 Unprocessable Entity</code></p>
<p><strong>Example 7: </strong><em>a PUT request for /api/antifraud/transaction</em></p>
<p><em>Request body:</em></p>
<pre><code class="language-json">{
   "transactionId": 2,
   "feedback": "MAY BE OK"
}</code></pre>
<p><em>Response: </em><code class="language-json">400 Bad Request</code></p>
<p><strong>Example 8: </strong><em>a PUT request for /api/antifraud/transaction</em></p>
<p><em>Request body:</em></p>
<pre><code class="language-json">{
   "transactionId": 1,
   "feedback": "PROHIBITED"
}</code></pre>
<p><em>Response: </em><code class="language-json">409 Conflict</code></p>
<p><strong>Example 9: </strong><em>a GET request for /api/antifraud/history/4000008449433402; the card number fails the Luhn algorithm</em></p>
<p><em>Response: </em><code class="language-json">400 Bad Request</code></p>
<p><strong>Example 10: </strong><em>a GET request for /api/antifraud/history/4000009455296122</em></p>
<p><em>Response: </em><code class="language-json">404 Not Found</code></p>
</div>