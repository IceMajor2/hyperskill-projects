<div class="step-text">
<h5 id="description">Description</h5>
<p>In the current implementation, our system is very basic; each transaction is considered in isolation from others. Let's add <strong>correlation</strong> to our fraud detection rules. Now, the result of the operation depends on other operations.</p>
<p>Let's enrich the transaction event with the world region and the transaction date. There is the table for world region codes:</p>
<table border="1" cellpadding="1" cellspacing="1" style="width: 500px;">
<thead>
<tr>
<th>Code</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr>
<td>EAP</td>
<td>East Asia and Pacific</td>
</tr>
<tr>
<td>ECA</td>
<td>Europe and Central Asia</td>
</tr>
<tr>
<td>HIC</td>
<td>High-Income countries</td>
</tr>
<tr>
<td>LAC</td>
<td>Latin America and the Caribbean</td>
</tr>
<tr>
<td>MENA</td>
<td>The Middle East and North Africa</td>
</tr>
<tr>
<td>SA</td>
<td>South Asia</td>
</tr>
<tr>
<td>SSA</td>
<td>Sub-Saharan Africa</td>
</tr>
</tbody>
</table>
<h5 id="objectives">Objectives</h5>
<ul>
<li>
<p>Implement the transaction history. Save ALL transactions, even PROHIBITED ones, in the database.</p>
</li>
<li>Change the <code class="language-json">POST /api/antifraud/transaction</code>. Now, the endpoint must accept data in the following JSON format:</li>
</ul>
<pre><code class="language-json">{
  "amount": &lt;Long&gt;,
  "ip": "&lt;String value, not empty&gt;",
  "number": "&lt;String value, not empty&gt;",
  "region": "&lt;String value, not empty&gt;",
  "date": "yyyy-MM-ddTHH:mm:ss"
}</code></pre>
<ul>
<li>Change the rules for reviewing a transaction. A transaction containing a card number is <code class="language-json">PROHIBITED</code> if:</li>
</ul>
<ol>
<li>There are transactions from more than 2 regions of the world other than the region of the transaction that is being verified in the last hour in the transaction history;</li>
<li>There are transactions from more than 2 unique IP addresses other than the IP of the transaction that is being verified in the last hour in the transaction history.</li>
</ol>
<ul>
<li>A transaction containing a card number is sent for <code class="language-json">MANUAL_PROCESSING</code> if:</li>
</ul>
<ol>
<li>There are transactions from 2 regions of the world other than the region of the transaction that is being verified in the last hour in the transaction history;</li>
<li>There are transactions from 2 unique IP addresses other than the IP of the transaction that is being verified in the last hour in the transaction history.</li>
</ol>
<ul>
<li>If the validation process was successful, the endpoint should respond with the status <code class="language-json">HTTP OK</code> (<code class="language-json">200</code>) and return the following JSON: </li>
</ul>
<pre><code class="language-json">{
  "result": &lt;String&gt;,
  "info": &lt;String&gt;
}</code></pre>
<p>If the <code class="language-json">result</code> is <code class="language-json">ALLOWED</code>, the <code class="language-json">info</code> field must be set to <code class="language-json">none</code>.</p>
<p>In the case of the <code class="language-json">PROHIBITED</code> or <code class="language-json">MANUAL_PROCESSING</code> result, the <code class="language-json">info</code> field must contain the reason for rejecting the transaction. The reason must be separated by <code class="language-json">,</code> and sorted alphabetically. For example, <code class="language-json">amount, card-number, ip, ip-correlation, region-correlation</code>.</p>
<p>If a request contains wrong data, the region and date must be validated as described above, but the endpoint should respond with the status <code class="language-json">HTTP Bad Request </code>(<code class="language-json">400</code>).</p>
<h5 id="examples">Examples</h5>
<p><strong>Example 1: </strong><em>a POST request for /api/antifraud/transaction</em></p>
<p><em>Request body:</em></p>
<pre><code class="language-json">{
  "amount": 150,
  "ip": "192.168.1.1",
  "number": "4000008449433403",
  "region": "EAP",
  "date": "2022-01-22T16:04:00"
}</code></pre>
<p><em>Response: </em><code class="language-json">200 OK</code></p>
<p><em>Response body:</em></p>
<pre><code class="language-json">{
   "result": "ALLOWED",
   "info": "none"
}</code></pre>
<p><strong>Example 2: </strong><em>a POST request for /api/antifraud/transaction; wrong date format</em></p>
<p><em>Request body:</em></p>
<pre><code class="language-json">{
  "amount": 150,
  "ip": "192.168.1.1",
  "number": "4000008449433403",
  "region": "EAP",
  "date": "2022-13-22T16:04:00"
}</code></pre>
<p><em>Response: </em><code class="language-json">400 BAD REQUEST</code></p>
<p><strong>Example 3: </strong><em>a POST request for /api/antifraud/transaction; wrong region</em></p>
<p><em>Request body:</em></p>
<pre><code class="language-json">{
  "amount": 150,
  "ip": "192.168.1.1",
  "number": "4000008449433403",
  "region": "EAPP",
  "date": "2022-13-22T16:04:00"
}</code></pre>
<p><em>Response: </em><code class="language-json">400 BAD REQUEST</code></p>
<p><strong>Example 4: </strong><em>a</em><strong> </strong><em>transaction </em><em>chain</em></p>
<p><em>#1</em></p>
<p><em>Request:</em></p>
<pre><code class="language-json">{
  "amount": 150,
  "ip": "192.168.1.1",
  "number": "4000008449433403",
  "region": "EAP",
  "date": "2022-12-22T16:04:00"
}</code></pre>
<p><em>Response:</em></p>
<pre><code class="language-json">{
   "result": "ALLOWED",
   "info": "none"
}</code></pre>
<p><em>#2</em></p>
<p><em>Request:</em></p>
<pre><code class="language-json">{
  "amount": 150,
  "ip": "192.168.1.2",
  "number": "4000008449433403",
  "region": "EAP",
  "date": "2022-12-22T16:05:00"
}</code></pre>
<p><em>Response:</em></p>
<pre><code class="language-json">{
   "result": "ALLOWED",
   "info": "none"
}</code></pre>
<p><em>#3</em></p>
<p><em>Request:</em></p>
<pre><code class="language-json">{
  "amount": 150,
  "ip": "192.168.1.2",
  "number": "4000008449433403",
  "region": "EAP",
  "date": "2022-12-22T16:06:00"
}</code></pre>
<p><em>Response; the IP has not changed:</em></p>
<pre><code class="language-json">{
   "result": "ALLOWED",
   "info": "none"
}</code></pre>
<p><em>#4</em></p>
<p><em>Request:</em></p>
<pre><code class="language-json">{
  "amount": 150,
  "ip": "192.168.1.3",
  "number": "4000008449433403",
  "region": "EAP",
  "date": "2022-12-22T16:07:00"
}</code></pre>
<p><em>Response:</em></p>
<pre><code class="language-json">{
   "result": "MANUAL_PROCESSING",
   "info": "ip-correlation"
}</code></pre>
<p><em>#5</em></p>
<p><em>Request:</em></p>
<pre><code class="language-json">{
  "amount": 150,
  "ip": "192.168.1.4",
  "number": "4000008449433403",
  "region": "EAP",
  "date": "2022-12-22T16:08:00"
}</code></pre>
<p><em>Response:</em></p>
<pre><code class="language-json">{
   "result": "PROHIBITTED",
   "info": "ip-correlation"
}</code></pre>
</div>