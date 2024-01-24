using System.Net.Http.Headers;
using System.Security.Claims;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Newtonsoft.Json;
using UserInterfaceWaterManager.Model.User;
using WaterManagerUI.Model.Item;

namespace WaterManagerUI.Pages;

public class GestoreAziendaPage : PageModel
{
    public GestoreAzienda user { get; set; }
    private readonly IHttpClientFactory _httpClientFactory;
    private readonly IHttpContextAccessor _httpContextAccessor;
    private readonly SignInManager<IdentityUser> _signInManager;
    public Model.Item.Azienda azienda { get; set; }


    public GestoreAziendaPage(IHttpClientFactory httpClientFactory,
        IHttpContextAccessor httpContextAccessor, SignInManager<IdentityUser> signInManager)
    {
        _httpClientFactory = httpClientFactory;
        _httpContextAccessor = httpContextAccessor;
        _signInManager = signInManager;
    }

    public async Task OnGetAsync()
    {
        var client = _httpClientFactory.CreateClient();

        if (_signInManager.IsSignedIn(User))
        {
            user = new GestoreAzienda(Convert.ToInt32(User.FindFirstValue(ClaimTypes.Gender)),
                User.FindFirstValue(ClaimTypes.Name), User.FindFirstValue(ClaimTypes.Surname),
                User.FindFirstValue(ClaimTypes.UserData), User.FindFirstValue(ClaimTypes.Email), "",
                JsonConvert.DeserializeObject<Model.Item.Azienda>(User.FindFirstValue((ClaimTypes.NameIdentifier))));

            if (user.azienda != null)
            {
                azienda = user.azienda;
            }
            else
            {
                try
                {
                    client.DefaultRequestHeaders.Authorization =
                        new AuthenticationHeaderValue("Bearer", User.FindFirstValue(ClaimTypes.Authentication));
                    var response = await client.GetAsync(
                        $"http://localhost:8080/api/v1/azienda/get/gestore/{user.id}");

                    if (response.IsSuccessStatusCode)
                    {
                        var jsonResponse = await response.Content.ReadAsStringAsync();
                        this.azienda = JsonConvert.DeserializeObject<Model.Item.Azienda>(jsonResponse);
                    }
                }
                catch (Exception e)
                {
                }
            }
        }
    }
}