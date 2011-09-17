package models;

import java.util.ArrayList;
import java.util.List;

import play.Logger;

import controllers.Constants;
import siena.Column;
import siena.Generator;
import siena.Id;
import siena.Index;
import siena.Json;
import siena.Max;
import siena.Model;
import siena.Query;
import siena.Table;
import siena.Text;

@Table("users")
public class User extends Model {
	
	@Id(Generator.UUID) @Max(36)
	public String id;
	
	@Column("four_square_id") @Index("four_square_id") @Max(100)
	public String fourSquareId;
	
	@Column("first_name") @Max(300)
	public String firstName;
	
	@Column("last_name") @Max(300)
	public String lastName;
	
	@Column("photo_url") @Max(300)
	public String photoUrl;
	
	public Json services; 
	
	@Text
	public String bio;
	
	
	public static User createUserFromItem(Json item) {
		User user = new User();
		if (item.containsKey("firstName") && !item.get("firstName").isNull())
			user.firstName = item.get("firstName").str();
		if (item.containsKey("lastName") && !item.get("lastName").isNull())
			user.lastName = item.get("lastName").str();
		if (item.containsKey("photo") && !item.get("photo").isNull())
			user.photoUrl = item.get("photo").str();
		if (item.containsKey("id") && !item.get("id").isNull())
			user.fourSquareId = item.get("id").str();
		if (item.containsKey("bio") && !item.get("bio").isNull())
			user.bio = item.get("bio").str();
		if (item.containsKey("services") && !item.get("services").isNull())
			user.services = item.get("services");
		
		return user;
	}
	
	public static Query<User> all() {
        return Model.all(User.class);
	}
	
	public List<String> getTags(int limit) {
		List<String> tags = new ArrayList<String>();
		if ( bio == null || bio.isEmpty()) return tags;
		
		String sBio = " " + bio.replaceAll(",", " ").replace('.', ' ').toLowerCase()+" ";
		for( String tag : Constants.tags){
			if(sBio.contains(tag)){
				tags.add(tag);
				sBio = sBio.replaceAll(tag, "");
			}
		}
		if (limit > 0 && tags.size() > limit) {
			return tags.subList(0, limit);
		}
		return tags;
	}
	
}
