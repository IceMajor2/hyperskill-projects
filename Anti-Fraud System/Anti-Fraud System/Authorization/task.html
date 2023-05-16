<div class="step-text">
<h5 id="description">Description</h5>
<p>An enterprise anti-fraud system has hundreds of merchant users who take advantage of the system by checking the validity of the transactions only. These users do not want to delve into a list of stolen cards, suspicious IP addresses, and who else uses the app. On the other hand, the number of users responsible for reporting stolen cards/IPs and excluding them from the blacklist is limited. These users don't have access to user-management functions. Finally, we have several users who are 100%-trusted and are allowed to access and modify more sensitive data.</p>
<p>In this stage, you need to add the <strong>authorization </strong>feature. Authorization is a process when the system decides whether an authenticated client has permission to access the requested resource. Authorization always follows authentication.</p>
<p>Let's implement the role model for our system:</p>
<table border="1" cellpadding="1" cellspacing="1" style="width: 800px;">
<thead>
<tr>
<th> </th>
<th>Anonymous</th>
<th>MERCHANT</th>
<th>ADMINISTRATOR</th>
<th>SUPPORT</th>
</tr>
</thead>
<tbody>
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
<td>PUT /api/auth/access</td>
<td>-</td>
<td>-</td>
<td>+</td>
<td>-</td>
</tr>
<tr>
<td>PUT /api/auth/role</td>
<td>-</td>
<td>-</td>
<td>+</td>
<td>-</td>
</tr>
</tbody>
</table>
<p>Let's talk about roles. <code class="language-json">ADMINISTRATOR</code> is the user who has registered first, all other users should receive the <code class="language-json">MERCHANT</code> roles. All users added after <code class="language-json">ADMINISTRATOR</code> must be locked by default and unlocked later by <code class="language-json">ADMINISTRATOR</code>. The <code class="language-json">SUPPORT</code> role should be assigned by <code class="language-json">ADMINISTRATOR</code> to one of the users later.</p>
<h5 id="objectives">Objectives</h5>
<ul>
<li>Add authorization to the service and implement the role model shown in the table above. The first registered user should receive the <code class="language-json">ADMINISTRATOR</code> role; the rest — <code class="language-json">MERCHANT</code>. In case of authorization violation, respond with the <code class="language-json">HTTP Forbidden</code> status (<code class="language-json">403</code>). Mind that only one role can be assigned to a user;</li>
<li>All users, except <code class="language-json">ADMINISTRATOR</code>, must be locked immediately after registration; only <code class="language-json">ADMINISTRATOR</code> can unlock users;</li>
<li>Change the response for the <code class="language-json">POST /api/auth/user</code> endpoint. It should respond with the <code class="language-json">HTTP Created</code> status (<code class="language-json">201</code>) and the body with the JSON object containing the information about a user. Add the <code class="language-json">role</code> field in the response:</li>
</ul>
<pre><code class="language-json">{
   "id": &lt;Long value, not empty&gt;,
   "name": "&lt;String value, not empty&gt;",
   "username": "&lt;String value, not empty&gt;",
   "role": "&lt;String value, not empty&gt;"
}</code></pre>
<ul>
<li>Change the response for the <code class="language-json">GET /api/auth/list</code> endpoint. Add the <code class="language-json">role</code> field in the response:</li>
</ul>
<pre><code class="language-json">[
    {
        "id": &lt;user1 id&gt;,
        "name": "&lt;user1 name&gt;",
        "username": "&lt;user1 username&gt;",
        "role": "&lt;user1 role&gt;"
    },
     ...
    {
        "id": &lt;userN id&gt;,
        "name": "&lt;userN name&gt;",
        "username": "&lt;userN username&gt;",
        "role": "&lt;userN role&gt;"
    }
]</code></pre>
<ul>
<li>Add the <code class="language-json">PUT /api/auth/role</code> endpoint that changes user roles. It must accept the following JSON body:</li>
</ul>
<pre><code class="language-json">{
   "username": "&lt;String value, not empty&gt;",
   "role": "&lt;String value, not empty&gt;"
}</code></pre>
<p>If successful, respond with the <code class="language-json">HTTP OK</code> status (<code class="language-json">200</code>) and the body like this:</p>
<pre><code class="language-json">{
   "id": &lt;Long value, not empty&gt;,
   "name": "&lt;String value, not empty&gt;",
   "username": "&lt;String value, not empty&gt;",
   "role": "&lt;String value, not empty&gt;"
}</code></pre>
<ul>
<li>If a user is not found, respond with the <code class="language-json">HTTP Not Found</code> status (<code class="language-json">404</code>);</li>
<li>If a role is not <code class="language-json">SUPPORT</code> or <code class="language-json">MERCHANT</code>, respond with <code class="language-json">HTTP Bad Request</code> status (<code class="language-json">400</code>);</li>
<li>If you want to assign a role that has been already provided to a user, respond with the <code class="language-json">HTTP Conflict</code> status (<code class="language-json">409</code>);</li>
<li>Add the <code class="language-json">PUT /api/auth/access</code> endpoint that locks/unlocks users. It accepts the following JSON body:</li>
</ul>
<pre><code class="language-json">{
   "username": "&lt;String value, not empty&gt;",
   "operation": "&lt;[LOCK, UNLOCK]&gt;"  // determines whether the user will be activated or deactivated
}</code></pre>
<p>If successful, respond with the <code class="language-json">HTTP OK</code> status (<code class="language-json">200</code>) and the following body:</p>
<pre><code class="language-json">{
    "status": "User &lt;username&gt; &lt;[locked, unlocked]&gt;!"
}</code></pre>
<ul>
<li>For safety reasons, <code class="language-json">ADMINISTRATOR</code> cannot be blocked. In this case, respond with the <code class="language-json">HTTP Bad Request</code> status (<code class="language-json">400</code>);</li>
<li>If a user is not found, the endpoint must respond with <code class="language-json">HTTP Not Found</code> status (<code class="language-json">404</code>).</li>
</ul>
<h5 id="examples">Examples</h5>
<p><strong>Example 1: </strong><em>a POST request for /api/auth/user with the correct user credentials</em></p>
<p><em>Request body:</em></p>
<pre><code class="language-json">{
   "name": "John Doe",
   "username": "JohnDoe",
   "password": "secret"
}</code></pre>
<p><em>Response: </em><code class="language-json">201 CREATED</code></p>
<p><em>Response body:</em></p>
<pre><code class="language-json">{
   "id": 1,
   "name": "John Doe",
   "username": "JohnDoe",
   "role": "ADMINISTRATOR"
}</code></pre>
<p><strong>Example 2: </strong><em>a GET request for /api/auth/list</em></p>
<p><em>R</em><em>esponse: </em><code class="language-json">200 OK</code></p>
<pre><code class="language-json">[
  {
    "name":"John Doe",
    "username":"JohnDoe",
    "role": "ADMINISTRATOR"
  },
  {
    "name":"JohnDoe2",
    "username":"JohnDoe2",
    "role": "MERCHANT"
  }
]</code></pre>
<p><strong>Example 3: </strong><em>a PUT request for /api/auth/role with the correct authentication under the ADMINISTRATOR role:</em></p>
<p><em>Request body:</em></p>
<pre><code class="language-json">{
   "username": "JohnDoe1",
   "role": "SUPPORT"
}</code></pre>
<p><em>Response: </em><code class="language-json">200 OK</code></p>
<p><em>Response body:</em></p>
<pre><code class="language-json">{
   "id": 1,
   "name": "John Doe 1",
   "username": "JohnDoe1",
   "role": "SUPPORT"
}</code></pre>
<p><strong>Example 4: </strong><em>a PUT request for /api/auth/role with the correct authentication under the ADMINISTRATOR role:</em></p>
<p><em>Request body:</em></p>
<pre><code class="language-json">{
   "username": "JohnDoe1",
   "role": "ADMINISTRATOR"
}</code></pre>
<p><em>Response: </em><code class="language-json">400 HTTP Bad Request</code></p>
<p><strong>Example 5: </strong><em>a PUT request for /api/auth/role with the correct authentication under the ADMINISTRATOR role:</em></p>
<p><em>Request body:</em></p>
<pre><code class="language-json">{
   "username": "JohnDoe1",
   "role": "SUPPORT"
}</code></pre>
<p><em>Response: </em><code class="language-json"><span style="color: #000000;">409 HTTP Conflict</span></code></p>
<p><strong>Example 6: </strong><em>a PUT request for /api/auth/access with the correct authentication under the ADMINISTRATOR role:</em></p>
<p><em>Request body:</em></p>
<pre><code class="language-json">{
   "username": "JohnDoe1",
   "operation": "UNLOCK"
}</code></pre>
<p><em>Response: </em><code class="language-json">200 OK</code></p>
<p><em>Response body:</em></p>
<pre><code class="language-json">{
   "status": "User JohnDoe1 unlocked!"
}</code></pre>
<p><strong>Example 7: </strong><em>a PUT request for /api/auth/access with the correct authentication under the ADMINISTRATOR role:</em></p>
<p><em>Request body:</em></p>
<pre><code class="language-json">{
   "username": "Administrator",
   "operation": "LOCK"
}</code></pre>
<p><em>Response: </em><code class="language-json">400 Bad Request</code></p>
</div>