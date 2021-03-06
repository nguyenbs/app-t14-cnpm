package org.team14.rubikonline.controller;

import java.time.OffsetDateTime;
import java.util.List;

import com.rethinkdb.net.Cursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.team14.rubikonline.database.RethinkDBConnectionFactory;
import org.team14.rubikonline.object.Record;
import com.rethinkdb.RethinkDB;

@RestController
@RequestMapping("/record")
public class Controller {
	@Autowired
	private RethinkDBConnectionFactory connectionFactory;

	@RequestMapping(method = RequestMethod.POST)
	public Record postRecord(@RequestBody Record record) {
		record.setDatetime(OffsetDateTime.now());
		RethinkDB.r.db("rubikonline").table("records").insert(record).run(connectionFactory.createConnection());
		return record;
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<Record> getRecord() {
		Cursor<Record> records = RethinkDB.r.db("rubikonline").table("records").orderBy().optArg("index","duration")
				.limit(5).run(connectionFactory.createConnection(), Record.class);
		return records.toList();
	}
}
