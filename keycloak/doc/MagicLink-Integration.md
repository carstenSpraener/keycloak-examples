# Integration of MagicLink Authentication

## Implementing the Authe

nticator

## Setup KeyCloak-Flow

To enable the Magic-Link authentication in KeyCloak you need to create a flow and bind 
this flow to the browser login flow.

The easiest way to create the flow is to duplicate the standard browser flow and add the
magiclink alternative to it. To duplicate the flow just click the menu buttons on the outer
right side of the row containing the browser flow and click _Duplicate_.

![keycloak-duplicate-flow.png](images%2Fkeycloak-duplicate-flow.png)

This will create a new Flow in the list of flows and directly opens the flow editor. In this
example i will desrcibe how to add the MagicLink as an alternative and not as a replacement
of the standard Username/Password flow. 

So you have add a new sub-flow with the button on the top:

![keycloak-add-sub-flow.png](images%2Fkeycloak-add-sub-flow.png)

Give the sub flow a descriptive name and click _Add_

![keycloak-name-sub-flow.png](images%2Fkeycloak-name-sub-flow.png)

Now there will be a new sub-flow in the login flow. Make sure it is marked as 
_Alternative_. Then open the _+_ menu and add _Add a Step_ . This will open a 
selector for all possible steps in the keycloak instance. The one required is 
the _Username form_. You can search for it by entering _Username_ in the search
field and hit _Enter_.

![keycloak-select-username-form.png](images%2Fkeycloak-select-username-form.png)

Select the username form and press _Add_.

Now the flow will ask for a Username. At a second step in the flow we need to
ask the MagicLinkAuthenticator. To do so, add another stept to the MagicLink-Flow
and search for "MagicLink" this time.

![keycloak-select-magiclink-authenticator.png](images%2Fkeycloak-select-magiclink-authenticator.png)

The whole magiclink flow should now look like this:

![keycloak-whole-magiclink-flow.png](images%2Fkeycloak-whole-magiclink-flow.png)

__Note:__ Make sure that the MagicLink-Login is marked as _Aternative_ and the 
_MagicLink Authentiator_ is _Required_!

At a last step bind this flow to the browser flow by opening the _Action_ menu in the
upper right corner and select _Bind flow_. In the upcoming dialog select the browser flow
and click _Save_.

![keycloak-final-flow-overview.png](images%2Fkeycloak-final-flow-overview.png)
